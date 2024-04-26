package com.xuecheng.ucenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author VectorX
 * @version V1.0
 * @description 搜索服务远程接口
 * @date 2024-04-26 14:47:03
 */
@FeignClient(value = "checkcode",
             fallbackFactory = CheckCodeClientFactory.class)
@RequestMapping("/checkcode")
public interface CheckCodeClient
{
    @PostMapping(value = "/verify")
    Boolean verify(
            @RequestParam("key")
                    String key,
            @RequestParam("code")
                    String code);
}
