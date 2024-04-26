package com.xuecheng.checkcode.service;

import com.xuecheng.checkcode.model.CheckCodeParamsDto;
import com.xuecheng.checkcode.model.CheckCodeResultDto;

/**
 * @author VectorX
 * @version 1.0.0
 * @description 验证码接口
 * @date 2024/04/25
 */
public interface CheckCodeService
{

    /**
     * 生成验证码
     *
     * @param checkCodeParamsDto 生成验证码参数
     * @return com.xuecheng.checkcode.model.CheckCodeResultDto 验证码结果
     */
    CheckCodeResultDto generate(CheckCodeParamsDto checkCodeParamsDto);

    /**
     * 校验验证码
     *
     * @param key
     * @param code
     * @return boolean
     */
    boolean verify(String key, String code);

    /**
     * @author VectorX
     * @version 1.0.0
     * @description 验证码生成器
     * @date 2024/04/25
     */
    interface CheckCodeGenerator
    {
        /**
         * 验证码生成
         *
         * @return 验证码
         */
        String generate(int length);

    }

    /**
     * @author VectorX
     * @version 1.0.0
     * @description key生成器
     * @date 2024/04/25
     */
    interface KeyGenerator
    {

        /**
         * key生成
         *
         * @return 验证码
         */
        String generate(String prefix);
    }

    /**
     * @author VectorX
     * @version 1.0.0
     * @description 验证码存储
     * @date 2024/04/25
     */
    interface CheckCodeStore
    {

        /**
         * 向缓存设置key
         *
         * @param key    key
         * @param value  value
         * @param expire 过期时间,单位秒
         */
        void set(String key, String value, Integer expire);

        String get(String key);

        void remove(String key);
    }
}
