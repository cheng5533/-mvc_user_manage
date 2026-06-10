package com.cheng.ming.mvc_user_manage;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 用户管理系统 - 主启动类
 */
@SpringBootApplication
public class MvcUserManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MvcUserManageApplication.class, args);
    }

    @Bean
    public CommandLineRunner generatePassword() {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
            String password = "123456";
            String hash = encoder.encode(password);
            System.out.println("========================================");
            System.out.println("明文密码: " + password);
            System.out.println("BCrypt哈希: " + hash);
            boolean matches = encoder.matches(password, hash);
            System.out.println("验证结果: " + matches);
            System.out.println("========================================");
        };
    }
}
