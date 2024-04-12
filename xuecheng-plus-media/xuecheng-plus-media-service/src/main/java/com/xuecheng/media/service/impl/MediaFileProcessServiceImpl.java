package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.mapper.MediaProcessHistoryMapper;
import com.xuecheng.media.mapper.MediaProcessMapper;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.model.po.MediaProcessHistory;
import com.xuecheng.media.service.MediaFileProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description 媒资文件处理业务方法
 * @date 2024-04-12 20:39:28
 */
@Service
@Slf4j
public class MediaFileProcessServiceImpl extends ServiceImpl<MediaProcessMapper, MediaProcess> implements MediaFileProcessService
{
    @Autowired
    private MediaFilesMapper mediaFilesMapper;

    @Autowired
    private MediaProcessHistoryMapper mediaProcessHistoryMapper;

    /**
     * 获取待处理任务
     *
     * @param shardTotal 分片总数
     * @param shardIndex 分片序号
     * @param count      获取记录数
     * @return java.util.List<com.xuecheng.media.model.po.MediaProcess>
     */
    @Override
    public List<MediaProcess> getMediaProcessList(int shardTotal, int shardIndex, int count) {
        return baseMapper.selectListByShardIndex(shardTotal, shardIndex, count);

    }

    /**
     * 开启一个任务
     *
     * @param id 任务id
     * @return true开启任务成功，false开启任务失败
     */
    @Override
    public boolean startTask(long id) {
        int result = baseMapper.startTask(id);
        return result <= 0 ?
               false :
               true;

    }

    /**
     * 保存任务结果
     *
     * @param taskId   任务id
     * @param status   任务状态
     * @param fileId   文件id
     * @param url      url
     * @param errorMsg 错误信息
     */
    @Override
    public void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg) {
        // 查出任务，如果不存在则直接返回
        MediaProcess mediaProcess = baseMapper.selectById(taskId);
        if (mediaProcess == null) {
            return;
        }

        // 处理失败，更新任务处理结果
        if (status.equals("3")) {
            MediaProcess mediaProcess_u = new MediaProcess();
            mediaProcess_u.setStatus("3");
            mediaProcess_u.setErrormsg(errorMsg);
            mediaProcess_u.setFailCount(mediaProcess.getFailCount() + 1);
            baseMapper.update(mediaProcess_u, new LambdaQueryWrapper<MediaProcess>().eq(MediaProcess::getId, taskId));
            log.debug("更新任务处理状态为失败，任务信息:{}", mediaProcess_u);
            return;
        }

        // 任务处理成功
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileId);
        if (mediaFiles != null) {
            // 更新媒资文件中的访问url
            mediaFiles.setUrl(url);
            mediaFilesMapper.updateById(mediaFiles);
        }

        // 处理成功，更新url和状态
        mediaProcess.setUrl(url);
        mediaProcess.setStatus("2");
        mediaProcess.setFinishDate(LocalDateTime.now());
        baseMapper.updateById(mediaProcess);

        // 添加到历史记录
        MediaProcessHistory mediaProcessHistory = new MediaProcessHistory();
        BeanUtils.copyProperties(mediaProcess, mediaProcessHistory);
        mediaProcessHistoryMapper.insert(mediaProcessHistory);

        // 删除mediaProcess
        baseMapper.deleteById(mediaProcess.getId());
    }
}
