package com.xuecheng.auth.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author VectorX
 * @version 1.0.0
 * @description Mybatis-Plus 配置
 * @date 2024/04/23
 */
@Configuration
@MapperScan("com.xuecheng.ucenter.mapper")
public class MybatisPlusConfig
{
    /**
     * 新的分页插件
     * <p>
     * 需要设置 MybatisConfiguration#useDeprecatedExecutor = false
     * <p>
     * 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     *
     * @return {@link MybatisPlusInterceptor}
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
