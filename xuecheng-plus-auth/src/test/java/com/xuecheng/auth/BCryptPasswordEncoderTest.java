package com.xuecheng.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author VectorX
 * @version V1.0
 * @description BCryptPasswordEncoder测试类
 * @date 2024-04-24 15:03:31
 */
public class BCryptPasswordEncoderTest
{
    public static void main(String[] args) {
        String password = "111111";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        for (int i = 0; i < 10; i++) {

            // 每个计算出的Hash值都不一样
            String hashPass = passwordEncoder.encode(password);
            System.out.println(hashPass);

            // 虽然每次计算的密码Hash值不一样但是校验是通过的
            boolean f = passwordEncoder.matches(password, hashPass);
            System.out.println(f);

        }
    }
}
