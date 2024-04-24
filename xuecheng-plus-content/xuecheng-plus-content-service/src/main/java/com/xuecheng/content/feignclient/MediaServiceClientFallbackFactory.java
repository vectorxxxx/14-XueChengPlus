package com.xuecheng.content.feignclient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-22 17:21:18
 */
@Slf4j
@Component
public class MediaServiceClientFallbackFactory implements FallbackFactory<MediaServiceClient>
{
    /**
     * 拿到了熔断的异常信息throwable
     *
     * @param throwable
     * @return {@link MediaServiceClient}
     */
    @Override
    public MediaServiceClient create(Throwable throwable) {
        return new MediaServiceClient()
        {
            /**
             * 发生熔断上传服务调用此方法执行降级逻辑
             *
             * @param filedata
             * @param objectName
             * @return {@link String}
             * @throws IOException
             */
            @Override
            public String upload(MultipartFile filedata, String objectName) throws IOException {
                log.debug("远程调用上传文件的接口发生熔断:{}", throwable.toString(), throwable);
                return null;
            }
        };
    }
}