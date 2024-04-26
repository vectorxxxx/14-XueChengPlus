package com.xuecheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
// 开启Feign客户端
@EnableFeignClients(basePackages = {"com.xuecheng.*.feignclient"})
public class AuthApplication
{

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
