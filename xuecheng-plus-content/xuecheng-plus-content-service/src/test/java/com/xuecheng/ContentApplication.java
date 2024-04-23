package com.xuecheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-08 09:51:41
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.xuecheng.content.feignclient"})
public class ContentApplication
{
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}
