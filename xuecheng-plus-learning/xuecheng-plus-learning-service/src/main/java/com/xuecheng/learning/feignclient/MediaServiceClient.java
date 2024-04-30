package com.xuecheng.learning.feignclient;

import com.xuecheng.base.model.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author VectorX
 * @version 1.0.0
 * @description
 * @date 2024/04/30
 */
@FeignClient(value = "media-api",
             fallbackFactory = MediaServiceClientFallbackFactory.class)
@RequestMapping("/media")
public interface MediaServiceClient
{

    @GetMapping("/open/preview/{mediaId}")
    RestResponse<String> getPlayUrlByMediaId(
            @PathVariable("mediaId")
                    String mediaId);

}
