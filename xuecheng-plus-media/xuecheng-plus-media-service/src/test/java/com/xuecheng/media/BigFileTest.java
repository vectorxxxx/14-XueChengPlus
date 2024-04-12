package com.xuecheng.media;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-11 15:56:25
 */
public class BigFileTest
{
    MinioClient minioClient = MinioClient
            .builder()
            .endpoint("http://192.168.56.14:9001")
            .credentials("minioadmin", "minioadmin")
            .build();

    /**
     * 将分块文件上传至minio
     */
    @Test
    public void uploadChunk() {
        // 分块文件夹
        File chunkFolder = new File("D:\\workspace-mine\\14-XueChengPlus\\backup\\chunk\\");

        // 分块文件
        List<File> fileList = Arrays.stream(chunkFolder.listFiles())
                                    // 从小到大排序
                                    .sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getName())))
                                    .collect(Collectors.toList());

        //将分块文件上传至minio
        for (int i = 0; i < fileList.size(); i++) {
            try {
                UploadObjectArgs uploadObjectArgs = UploadObjectArgs
                        .builder()
                        .bucket("testbucket")
                        .object("chunk/" + i)
                        .filename(fileList
                                .get(i)
                                .getAbsolutePath())
                        .build();
                minioClient.uploadObject(uploadObjectArgs);
                System.out.println("上传分块成功" + i);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 合并文件，要求分块文件最小5M
     *
     * @throws Exception
     */
    @Test
    public void test_merge() throws Exception {
        List<ComposeSource> sources = Stream
                .iterate(0, i -> ++i)
                .limit(6)
                .map(i -> ComposeSource
                        .builder()
                        .bucket("testbucket")
                        .object("chunk/".concat(Integer.toString(i)))
                        .build())
                .collect(Collectors.toList());

        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs
                .builder()
                .bucket("testbucket")
                .object("merge01.mp4")
                .sources(sources)
                .build();
        minioClient.composeObject(composeObjectArgs);
    }

    /**
     * 清除分块文件
     */
    @Test
    public void test_removeObjects() {
        // 合并分块完成将分块文件清除
        List<DeleteObject> deleteObjects = Stream
                .iterate(0, i -> ++i)
                .limit(6)
                .map(i -> new DeleteObject("chunk/".concat(Integer.toString(i))))
                .collect(Collectors.toList());

        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs
                .builder()
                .bucket("testbucket")
                .objects(deleteObjects)
                .build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
        results.forEach(r -> {
            try {
                DeleteError deleteError = r.get();
                System.out.println(deleteError.message());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 测试文件分块方法
     *
     * @throws IOException
     */
    @Test
    public void testChunk() throws IOException {
        // 源文件
        File sourceFile = new File("D:/workspace-mine/14-XueChengPlus/backup/org.mp4");

        // 目标文件夹
        String chunkPath = "D:/workspace-mine/14-XueChengPlus/backup/chunk/";
        File chunkFolder = new File(chunkPath);
        if (!chunkFolder.exists()) {
            chunkFolder.mkdirs();
        }

        // 分块大小
        long chunkSize = 1024 * 1024 * 5;
        // 分块数量
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        System.out.println("分块总数：" + chunkNum);

        // 缓冲区大小
        byte[] b = new byte[1024];
        // 使用RandomAccessFile访问文件
        try (RandomAccessFile rafRead = new RandomAccessFile(sourceFile, "r")) {
            // 分块
            for (int i = 0; i < chunkNum; i++) {
                // 创建分块文件
                File file = new File(chunkPath + i);
                if (file.exists()) {
                    file.delete();
                }
                boolean newFile = file.createNewFile();
                if (!newFile) {
                    continue;
                }

                // 向分块文件中写数据
                try (RandomAccessFile rafWrite = new RandomAccessFile(file, "rw")) {
                    int len;
                    while ((len = rafRead.read(b)) != -1) {
                        rafWrite.write(b, 0, len);
                        if (file.length() >= chunkSize) {
                            break;
                        }
                    }
                }
                System.out.println("完成分块" + i);
            }
        }
    }

    /**
     * 测试文件合并方法
     *
     * @throws IOException
     */
    @Test
    public void testMerge() throws IOException {
        // 块文件目录
        File chunkFolder = new File("D:/workspace-mine/14-XueChengPlus/backup/chunk/");

        // 原始文件
        File originalFile = new File("D:/workspace-mine/14-XueChengPlus/backup/org.mp4");

        // 合并文件
        File mergeFile = new File("D:/workspace-mine/14-XueChengPlus/backup/merge.mp4");
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        // 创建新的合并文件
        mergeFile.createNewFile();

        // 用于写文件
        try (RandomAccessFile rafWrite = new RandomAccessFile(mergeFile, "rw")) {
            // 分块列表
            File[] fileArray = chunkFolder.listFiles();
            // 转成集合，便于排序
            List<File> fileList = Arrays.stream(fileArray)
                                        // 从小到大排序
                                        .sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getName())))
                                        .collect(Collectors.toList());

            // 指针指向文件顶端
            rafWrite.seek(0);
            // 缓冲区
            byte[] b = new byte[1024];

            // 合并文件
            for (File chunkFile : fileList) {
                try (RandomAccessFile rafRead = new RandomAccessFile(chunkFile, "rw")) {
                    int len;
                    while ((len = rafRead.read(b)) != -1) {
                        rafWrite.write(b, 0, len);
                    }
                }
            }
        }

        //校验文件
        try (FileInputStream fileInputStream = new FileInputStream(originalFile);
             FileInputStream mergeFileStream = new FileInputStream(mergeFile)) {
            //取出原始文件的md5
            String originalMd5 = DigestUtils.md5Hex(fileInputStream);
            //取出合并文件的md5进行比较
            String mergeFileMd5 = DigestUtils.md5Hex(mergeFileStream);
            System.out.println(originalMd5.equals(mergeFileMd5) ?
                               "合并文件成功" :
                               "合并文件失败");
        }
    }

}
