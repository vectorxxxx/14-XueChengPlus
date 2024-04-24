package com.xuecheng.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author VectorX
 * @version 1.0.0
 * @description 网关安全配置类
 * @date 2024/04/24
 */
// 启用基于WebFlux的安全性配置
@EnableWebFluxSecurity
@Configuration
public class SecurityConfig
{

    /**
     * 安全拦截配置
     *
     * @param http
     * @return {@link SecurityWebFilterChain}
     */
    @Bean
    public SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .pathMatchers("/**")
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .csrf()
                .disable()
                .build();
    }

}
