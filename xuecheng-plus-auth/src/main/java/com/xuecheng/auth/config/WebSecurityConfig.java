package com.xuecheng.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author VectorX
 * @version 1.0.0
 * @description 认证管理配置
 * @date 2024/04/23
 * @see WebSecurityConfigurerAdapter
 */
// 启用Spring Security的Web安全功能
@EnableWebSecurity
// 启用Spring Security的方法级安全功能，允许在方法级别上配置安全规则
@EnableGlobalMethodSecurity(securedEnabled = true,
                            prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

    /**
     * 配置用户信息服务
     *
     * @return {@link UserDetailsService}
     */
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        // 这里配置用户信息,这里暂时使用这种方式将用户存储在内存中
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User
                .withUsername("zhangsan")
                .password("123")
                .authorities("p1")
                .build());
        manager.createUser(User
                .withUsername("lisi")
                .password("456")
                .authorities("p2")
                .build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 密码为明文方式
        return NoOpPasswordEncoder.getInstance();
        // 密码为密文方式
        // return new BCryptPasswordEncoder();
    }

    /**
     * 配置安全拦截机制
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/r/**")
                .authenticated()//访问/r开始的请求需要认证通过
                .anyRequest()
                .permitAll()//其它请求全部放行
                .and()
                .formLogin()
                .successForwardUrl("/login-success")//登录成功跳转到/login-success
                .and()
                .logout()
                .logoutUrl("/logout");//退出地址
    }

    /**
     * 配置认证管理器
     *
     * @return {@link AuthenticationManager}
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
