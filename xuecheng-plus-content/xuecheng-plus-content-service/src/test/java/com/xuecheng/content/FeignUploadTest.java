package com.xuecheng.content;

import com.xuecheng.content.config.MultipartSupportConfig;
import com.xuecheng.content.feignclient.MediaServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author VectorX
 * @version V1.0
 * @description 测试使用feign远程上传文件
 * @date 2024-04-22 16:47:45
 */
@SpringBootTest
public class FeignUploadTest
{
    @Autowired
    private MediaServiceClient mediaServiceClient;

    //远程调用，上传文件
    @Test
    public void test() throws IOException {
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(
                new File("D:\\workspace-mine\\14-XueChengPlus\\xuecheng-plus-content\\xuecheng-plus-content-service\\src\\test\\resources\\templates\\course_template.html"));
        mediaServiceClient.upload(multipartFile, "course/128.html");
    }
}
