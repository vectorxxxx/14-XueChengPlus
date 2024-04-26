package com.xuecheng.checkcode.service.impl;

import com.xuecheng.checkcode.service.CheckCodeService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VectorX
 * @version 1.0.0
 * @description 使用本地内存存储验证码，测试用
 * @date 2024/04/25
 * @see CheckCodeService.CheckCodeStore
 */
@Component("MemoryCheckCodeStore")
public class MemoryCheckCodeStore implements CheckCodeService.CheckCodeStore
{

    Map<String, String> map = new HashMap<String, String>();

    @Override
    public void set(String key, String value, Integer expire) {
        map.put(key, value);
    }

    @Override
    public String get(String key) {
        return map.get(key);
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }
}
