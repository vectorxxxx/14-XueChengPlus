package com.xuecheng.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.mapper.XcUserRoleMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.model.po.XcUserRole;
import com.xuecheng.ucenter.service.AuthService;
import com.xuecheng.ucenter.service.WxAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author VectorX
 * @version V1.0
 * @description 微信扫码认证
 * @date 2024-04-26 16:09:58
 */
@Slf4j
@Service("wx_authservice")
public class WxAuthServiceImpl implements AuthService, WxAuthService
{

    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.secret}")
    private String secret;

    @Autowired
    private XcUserMapper xcUserMapper;

    @Autowired
    private XcUserRoleMapper xcUserRoleMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WxAuthServiceImpl currentProxy;

    /**
     * 微信认证
     *
     * @param code
     * @return {@link XcUser}
     */
    @Override
    public XcUser wxAuth(String code) {
        // 1、收到code调用微信接口申请access_token
        Map<String, String> access_token_map = getAccess_token(code);
        if (access_token_map == null) {
            return null;
        }
        System.out.println(access_token_map);

        // 2、获取用户信息
        String openid = access_token_map.get("openid");
        String access_token = access_token_map.get("access_token");
        // 拿access_token查询用户信息
        Map<String, String> userinfo = getUserinfo(access_token, openid);
        if (userinfo == null) {
            return null;
        }

        // 3、将用户信息保存到数据库
        return currentProxy.addWxUser(userinfo);
    }

    /**
     * 申请访问令牌,响应示例
     * <pre>{@literal
     * {
     *     "access_token": "ACCESS_TOKEN",
     *     "expires_in": 7200,
     *     "refresh_token": "REFRESH_TOKEN",
     *     "openid": "OPENID",
     *     "scope": "SCOPE",
     *     "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     * }</pre>
     */
    private Map<String, String> getAccess_token(String code) {
        // 1、请求微信地址
        String wxUrl_template = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
        String wxUrl = String.format(wxUrl_template, appid, secret, code);
        log.info("调用微信接口申请access_token, url:{}", wxUrl);

        // 2、调用微信接口
        String result = restTemplate
                .exchange(wxUrl, HttpMethod.POST, null, String.class)
                .getBody();
        log.info("调用微信接口申请access_token: 返回值:{}", result);

        // 3、解析返回值
        return JSON.parseObject(result, new TypeReference<Map<String, String>>() {});
    }

    /**
     * 获取用户信息，示例如下：
     * <pre>{@literal
     * {
     *     "openid": "OPENID",
     *     "nickname": "NICKNAME",
     *     "sex": 1,
     *     "province": "PROVINCE",
     *     "city": "CITY",
     *     "country": "COUNTRY",
     *     "headimgurl": "https://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
     *     "privilege": [
     *         "PRIVILEGE1",
     *         "PRIVILEGE2"
     *     ],
     *     "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     * }</pre>
     */
    private Map<String, String> getUserinfo(String access_token, String openid) {
        // 1、请求微信地址
        String wxUrl_template = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
        String wxUrl = String.format(wxUrl_template, access_token, openid);
        log.info("调用微信接口申请userinfo, url:{}", wxUrl);

        // 2、调用微信接口
        final String body = restTemplate
                .exchange(wxUrl, HttpMethod.POST, null, String.class)
                .getBody();
        // 防止乱码进行转码
        String result = new String(Objects
                .requireNonNull(body)
                .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        log.info("调用微信接口申请userinfo: 返回值:{}", result);

        // 3、解析返回值
        return JSON.parseObject(result, new TypeReference<Map<String, String>>() {});
    }

    /**
     * 添加用户
     *
     * @param userInfo_map
     * @return {@link XcUser}
     */
    @Transactional
    public XcUser addWxUser(Map<String, String> userInfo_map) {
        // 1、根据unionid查询数据库
        String unionid = userInfo_map.get("unionid");
        XcUser xcUser = xcUserMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getWxUnionid, unionid));
        if (xcUser != null) {
            return xcUser;
        }

        String userId = UUID
                .randomUUID()
                .toString();
        log.info("添加用户:{}", userId);

        // 2、添加用户
        xcUser = new XcUser();
        xcUser.setId(userId);
        xcUser.setWxUnionid(unionid);
        // 记录从微信得到的昵称
        xcUser.setNickname(userInfo_map.get("nickname"));
        xcUser.setUserpic(userInfo_map.get("headimgurl"));
        xcUser.setName(userInfo_map.get("nickname"));
        xcUser.setUsername(unionid);
        xcUser.setPassword(unionid);
        xcUser.setUtype("101001");//学生类型
        xcUser.setStatus("1");//用户状态
        xcUser.setCreateTime(LocalDateTime.now());
        xcUserMapper.insert(xcUser);

        // 3、添加用户角色
        XcUserRole xcUserRole = new XcUserRole();
        xcUserRole.setId(UUID
                .randomUUID()
                .toString());
        xcUserRole.setUserId(userId);
        xcUserRole.setRoleId("17");//学生角色
        xcUserRole.setCreateTime(LocalDateTime.now());
        xcUserRoleMapper.insert(xcUserRole);

        return xcUser;
    }

    /**
     * 认证方法
     *
     * @param authParamsDto 认证参数
     * @return com.xuecheng.ucenter.model.po.XcUser 用户信息
     */
    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {
        // 1、校验账号
        String username = authParamsDto.getUsername();
        XcUser user = xcUserMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, username));
        // 返回空表示用户不存在
        if (user == null) {
            throw new RuntimeException("账号不存在");
        }

        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(user, xcUserExt);
        return xcUserExt;
    }
}
