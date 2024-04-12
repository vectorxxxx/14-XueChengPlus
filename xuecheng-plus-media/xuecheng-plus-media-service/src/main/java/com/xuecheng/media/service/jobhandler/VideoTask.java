package com.xuecheng.media.service.jobhandler;

import com.xuecheng.base.utils.Mp4VideoUtil;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaFileProcessService;
import com.xuecheng.media.service.MediaFileService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-12 20:56:05
 */
@Slf4j
@Component
public class VideoTask
{
    @Autowired
    private MediaFileService mediaFileService;
    @Autowired
    private MediaFileProcessService mediaFileProcessService;

    @Value("${videoprocess.ffmpegpath}")
    private String ffmpegpath;

    @XxlJob("videoJobHandler")
    public void videoJobHandler() throws Exception {
        // ===查询待处理任务===
        List<MediaProcess> mediaProcessList = getMediaProcesses();
        if (CollectionUtils.isEmpty(mediaProcessList)) {
            return;
        }
        final int size = mediaProcessList.size();
        log.debug("取出待处理视频任务{}条", size);

        // 启动size个线程的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(size);
        // 计数器
        CountDownLatch countDownLatch = new CountDownLatch(size);
        // 将处理任务加入线程池
        mediaProcessList.forEach(mediaProcess -> {
            threadPool.execute(() -> {
                resolveTask(mediaProcess, countDownLatch);
            });
        });
        // 等待,给一个充裕的超时时间,防止无限等待，到达超时时间还没有处理完成则结束任务
        countDownLatch.await(30, TimeUnit.MINUTES);
    }

    /**
     * 查询待处理任务
     *
     * @return {@link List}<{@link MediaProcess}>
     */
    private List<MediaProcess> getMediaProcesses() {
        List<MediaProcess> mediaProcessList;
        try {
            // 分片参数
            int shardTotal = XxlJobHelper.getShardTotal();
            int shardIndex = XxlJobHelper.getShardIndex();
            // 取出cpu核心数作为一次处理数据的条数
            int processors = Runtime
                    .getRuntime()
                    .availableProcessors();

            // 一次处理视频数量不要超过cpu核心数
            mediaProcessList = mediaFileProcessService.getMediaProcessList(shardTotal, shardIndex, processors);
        }
        catch (Exception e) {
            log.error("获取待处理视频任务失败:{}", e.getMessage(), e);
            return null;
        }
        return mediaProcessList;
    }

    /**
     * 处理任务
     *
     * @param mediaProcess
     * @param countDownLatch
     */
    private void resolveTask(MediaProcess mediaProcess, CountDownLatch countDownLatch) {
        File mp4File = null;
        try {
            // 任务id
            Long taskId = mediaProcess.getId();

            // ===1、抢占任务===
            if (preemptTask(mediaProcess, taskId)) {
                return;
            }

            // 桶
            String bucket = mediaProcess.getBucket();
            // 存储路径
            String filePath = mediaProcess.getFilePath();
            // 原始视频的md5值
            String fileId = mediaProcess.getFileId();

            // ===2、下载待处理文件到服务器上===
            File originalFile = downloadFileFromMinIO(taskId, bucket, filePath, fileId);
            if (originalFile == null) {
                return;
            }

            // ===3、创建临时文件===
            mp4File = createTempFile(taskId, fileId);
            if (mp4File == null) {
                return;
            }

            // ===4、视频处理结果===
            if (transformVideo(mediaProcess, taskId, bucket, filePath, fileId, originalFile, mp4File)) {
                return;
            }

            // ===5、mp4上传至minio===
            uploadVideo(taskId, bucket, fileId, mp4File);
        }
        finally {
            if (mp4File != null) {
                // 删除临时文件
                mp4File.delete();
            }
            countDownLatch.countDown();
        }
    }

    /**
     * 1、抢占任务
     *
     * @param mediaProcess
     * @param taskId
     * @return boolean
     */
    private boolean preemptTask(MediaProcess mediaProcess, Long taskId) {
        boolean b = mediaFileProcessService.startTask(taskId);
        if (!b) {
            return true;
        }
        log.debug("开始执行任务:{}", mediaProcess);
        return false;
    }

    /**
     * 2、下载待处理文件到服务器上
     *
     * @param taskId
     * @param bucket
     * @param filePath
     * @param fileId
     * @return {@link File}
     */
    private File downloadFileFromMinIO(Long taskId, String bucket, String filePath, String fileId) {
        File originalFile = mediaFileService.downloadFileFromMinIO(bucket, filePath);
        if (originalFile == null) {
            log.debug("下载待处理文件失败,originalFile:{}", bucket.concat(filePath));
            mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "下载待处理文件失败");
            return null;
        }
        return originalFile;
    }

    /**
     * 3、创建临时文件
     *
     * @param taskId
     * @param fileId
     * @return {@link File}
     */
    private File createTempFile(Long taskId, String fileId) {
        File mp4File;
        try {
            mp4File = File.createTempFile("mp4", ".mp4");
        }
        catch (IOException e) {
            log.error("创建mp4临时文件失败:{}", e.getMessage(), e);
            mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "创建mp4临时文件失败");
            return null;
        }
        return mp4File;
    }

    /**
     * 4、视频处理结果
     *
     * @param mediaProcess
     * @param taskId
     * @param bucket
     * @param filePath
     * @param fileId
     * @param originalFile
     * @param mp4File
     * @return boolean
     */
    private boolean transformVideo(MediaProcess mediaProcess, Long taskId, String bucket, String filePath, String fileId, File originalFile, File mp4File) {
        String result = "";
        try {
            // 开始视频转换，成功将返回success
            result = new Mp4VideoUtil(ffmpegpath, originalFile.getAbsolutePath(), mp4File.getName(), mp4File.getAbsolutePath()).generateMp4();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("处理视频文件:{},出错:{}", mediaProcess.getFilePath(), e.getMessage());
        }

        if (!result.equals("success")) {
            // 记录错误信息
            log.error("处理视频失败,视频地址:{},错误信息:{}", bucket + filePath, result);
            mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "视频处理失败");
            return true;
        }
        return false;
    }

    /**
     * 5、mp4上传至minio
     *
     * @param taskId
     * @param bucket
     * @param fileId
     * @param mp4File
     */
    private void uploadVideo(Long taskId, String bucket, String fileId, File mp4File) {
        // mp4在minio的存储路径
        final String objectName = getFilePath(fileId, ".mp4");
        // 访问url
        final String url = "/" + bucket + "/" + objectName;
        try {
            mediaFileService.addMediaFilesToMinIO(mp4File.getAbsolutePath(), "video/mp4", bucket, objectName);

            // 将url存储至数据，并更新状态为成功，并将待处理视频记录删除存入历史
            mediaFileProcessService.saveProcessFinishStatus(taskId, "2", fileId, url, null);
        }
        catch (Exception e) {
            log.error("上传视频失败或入库失败,视频地址:{},错误信息:{}", bucket + objectName, e.getMessage());

            // 最终还是失败了
            mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "处理后视频上传或入库失败");
        }
    }

    private String getFilePath(String fileMd5, String fileExt) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + fileMd5 + fileExt;
    }
}
