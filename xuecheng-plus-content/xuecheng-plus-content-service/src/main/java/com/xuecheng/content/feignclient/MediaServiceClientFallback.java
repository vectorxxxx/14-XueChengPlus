package com.xuecheng.content.feignclient;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-22 17:20:13
 */
public class MediaServiceClientFallback implements MediaServiceClient
{
    @Override
    public String upload(MultipartFile filedata, String objectName) throws IOException {
        return null;
    }
}
