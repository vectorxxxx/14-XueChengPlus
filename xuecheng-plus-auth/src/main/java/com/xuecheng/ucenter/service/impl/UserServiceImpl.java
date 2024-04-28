package com.xuecheng.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.ucenter.mapper.XcMenuMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcMenu;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-24 14:59:23
 */
@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService
{
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private XcMenuMapper menuMapper;

    /**
     * 根据账号查询用户信息
     *
     * @param s 账号 兼容多种方式 1、用户名-密码 2、手机号-验证码 3、微信-openid
     * @return {@link UserDetails}
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AuthParamsDto authParamsDto;
        try {
            // 将认证参数转为AuthParamsDto类型
            authParamsDto = JSON.parseObject(s, AuthParamsDto.class);
        }
        catch (Exception e) {
            log.info("认证请求不符合项目要求:{}", s);
            throw new RuntimeException("认证请求数据格式不对");
        }

        // 认证类型，有password，wx。。。
        final String authType = authParamsDto.getAuthType();
        // 根据认证类型从spring容器取出指定的bean
        final String beanName = authType + "_authservice";
        final AuthService authService = applicationContext.getBean(beanName, AuthService.class);
        // 统一调用execute方法完成认证
        final XcUserExt xcUserExt = authService.execute(authParamsDto);

        // 查询用户信息
        return getUserDetails(xcUserExt);
    }

    /**
     * 查询用户信息
     *
     * @param user
     * @return {@link UserDetails}
     */
    private UserDetails getUserDetails(XcUser user) {
        // 取出数据库存储的正确密码
        final String password = user.getPassword();
        // 为了安全在令牌中不放密码
        user.setPassword(null);
        // 将user对象转json
        String userString = JSON.toJSONString(user);
        // 用户权限,如果不加报Cannot pass a null GrantedAuthority collection
        String[] authorities = {"p1"};
        // 查询用户权限
        List<XcMenu> xcMenus = menuMapper.selectPermissionByUserId(user.getId());
        if (!CollectionUtils.isEmpty(xcMenus)) {
            authorities = xcMenus
                    .stream()
                    .map(XcMenu::getCode)
                    .toArray(String[]::new);
        }

        // 创建UserDetails对象,权限信息待实现授权功能时再向UserDetail中加入
        return User
                // .withUsername(user.getUsername())
                .withUsername(userString)
                .password(password)
                .authorities(authorities)
                .build();
    }

}
