package com.xuecheng;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author VectorX
 * @version V1.0
 * @description 启动类
 * @date 2024-04-07 17:35:22
 */
@SpringBootApplication
@EnableSwagger2Doc
public class ContentApplication
{
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}
