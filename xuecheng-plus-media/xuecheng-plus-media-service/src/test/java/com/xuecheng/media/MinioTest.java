package com.xuecheng.media;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;

/**
 * @author Mr.M
 * @version 1.0
 * @description 测试minio的sdk
 * @date 2023/2/17 11:55
 */
public class MinioTest
{

    MinioClient minioClient = MinioClient
            .builder()
            .endpoint("http://192.168.56.14:9001")
            .credentials("minioadmin", "minioadmin")
            .build();

    @Test
    public void test_upload() throws Exception {

        // 通过扩展名得到媒体资源类型 mimeType
        // 根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(".mp4");
        // 通用mimeType，字节流
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if (extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }

        //上传文件的参数信息
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs
                .builder()
                .bucket("testbucket")//桶
                .filename("D:\\workspace-mine\\14-XueChengPlus\\backup\\1.mp4") //指定本地文件路径
                .object("test/01/1.mp4")//对象名 放在子目录下
                .contentType(mimeType)//设置媒体文件类型
                .build();

        //上传文件
        minioClient.uploadObject(uploadObjectArgs);

    }

    //删除文件
    @Test
    public void test_delete() throws Exception {

        //RemoveObjectArgs
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs
                .builder()
                .bucket("testbucket")
                .object("test/01/1.mp4")
                .build();

        //删除文件
        minioClient.removeObject(removeObjectArgs);

    }

    //查询文件 从minio中下载
    @Test
    public void test_getFile() throws Exception {

        GetObjectArgs getObjectArgs = GetObjectArgs
                .builder()
                .bucket("testbucket")
                .object("test/01/1.mp4")
                .build();
        //查询远程服务获取到一个流对象
        FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
        //指定输出流
        FileOutputStream outputStream = new FileOutputStream("D:\\workspace-mine\\14-XueChengPlus\\backup\\1a.mp4");
        IOUtils.copy(inputStream, outputStream);

        //校验文件的完整性对文件的内容进行md5
        String source_md5 = DigestUtils.md5Hex(new FileInputStream("D:\\workspace-mine\\14-XueChengPlus\\backup\\1.mp4"));
        String local_md5 = DigestUtils.md5Hex(new FileInputStream("D:\\workspace-mine\\14-XueChengPlus\\backup\\1a.mp4"));
        if (source_md5.equals(local_md5)) {
            System.out.println("下载成功");
        }

    }

}