package com.xuecheng.checkcode.service.impl;

import com.xuecheng.checkcode.service.CheckCodeService;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author VectorX
 * @version 1.0.0
 * @description uuid生成器
 * @date 2024/04/25
 * @see CheckCodeService.KeyGenerator
 */
@Component("UUIDKeyGenerator")
public class UUIDKeyGenerator implements CheckCodeService.KeyGenerator
{
    @Override
    public String generate(String prefix) {
        String uuid = UUID
                .randomUUID()
                .toString();
        return prefix + uuid.replaceAll("-", "");
    }
}
