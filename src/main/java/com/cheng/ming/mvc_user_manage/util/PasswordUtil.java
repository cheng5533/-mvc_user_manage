package com.cheng.ming.mvc_user_manage.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码加密工具类
 * 使用 BCrypt 算法，自动加盐，防止彩虹表攻击
 */
@Component
public class PasswordUtil {

    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordUtil() {
        // strength 参数表示计算复杂度，范围 4-31，默认 10
        // 数值越大越安全，但速度越慢
        this.passwordEncoder = new BCryptPasswordEncoder(10);
    }

    /**
     * 加密密码
     * @param rawPassword 明文密码
     * @return 加密后的密码（包含盐值）
     */
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 验证密码
     * @param rawPassword 明文密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
