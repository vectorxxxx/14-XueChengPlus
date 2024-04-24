package com.xuecheng.content.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author VectorX
 * @version V1.0
 * @description 资源服务配置
 * @date 2024-04-24 11:21:23
 */
@Configuration
// 配置资源服务器
@EnableResourceServer
// 启用全局方法级安全性控制
@EnableGlobalMethodSecurity(
        // 启用 @Secured 注解，该注解可用于对方法进行基于角色的访问控制。
        // 只有具有指定角色的用户才能调用被 @Secured 注解标记的方法。
        securedEnabled = true,
        // 启用 @PreAuthorize 和 @PostAuthorize 注解，这两个注解可以用于更灵活的方法级安全性控制。
        // @PreAuthorize 注解用于在方法执行前进行访问控制检查
        // @PostAuthorize 注解用于在方法执行后进行访问控制检查
        prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter
{

    /**
     * 资源服务标识
     */
    public static final String RESOURCE_ID = "xuecheng-plus";

    @Autowired
    TokenStore tokenStore;

    /**
     * 配置安全配置
     *
     * @param resources
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID)//资源 id
                 .tokenStore(tokenStore)
                 .stateless(true); // 无状态
    }

    /**
     * 配置安全拦截机制
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/r/**", "/course/**")
                .authenticated()//所有/r/**的请求必须认证通过
                .anyRequest()
                .permitAll();
    }
}
