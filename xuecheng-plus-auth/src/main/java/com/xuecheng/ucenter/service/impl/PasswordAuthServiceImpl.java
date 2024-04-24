package com.xuecheng.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.AuthService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author VectorX
 * @version V1.0
 * @description 账号密码认证
 * @date 2024-04-24 17:24:17
 */
@Service("password_authservice")
public class PasswordAuthServiceImpl implements AuthService
{
    @Autowired
    private XcUserMapper xcUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 认证方法
     *
     * @param authParamsDto 认证参数
     * @return com.xuecheng.ucenter.model.po.XcUser 用户信息
     */
    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {
        XcUser user = xcUserMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, authParamsDto.getUsername()));
        // 返回空表示用户不存在
        if (user == null) {
            throw new RuntimeException("账号不存在");
        }

        // 取出数据库存储的正确密码
        String passwordDb = user.getPassword();
        String passwordForm = authParamsDto.getPassword();
        // 校验密码
        if (!passwordEncoder.matches(passwordForm, passwordDb)) {
            throw new RuntimeException("账号或密码错误");
        }

        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(user, xcUserExt);
        return xcUserExt;
    }
}
