package com.xuecheng.content.feignclient;

import com.xuecheng.content.config.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author VectorX
 * @version V1.0
 * @description 媒资管理服务远程接口
 * @date 2024-04-22 16:45:56
 */
@FeignClient(value = "media-api",
             configuration = {MultipartSupportConfig.class},
             fallbackFactory = MediaServiceClientFallbackFactory.class)
public interface MediaServiceClient
{

    @RequestMapping(value = "/media/upload/coursefile",
                    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String upload(
            @RequestPart("filedata")
                    MultipartFile filedata,
            @RequestParam(value = "objectName",
                          required = false)
                    String objectName) throws IOException;
}

