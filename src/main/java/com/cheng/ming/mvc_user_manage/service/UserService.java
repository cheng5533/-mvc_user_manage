package com.cheng.ming.mvc_user_manage.service;

import com.cheng.ming.mvc_user_manage.entity.User;
import com.cheng.ming.mvc_user_manage.repository.UserRepository;
import com.cheng.ming.mvc_user_manage.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户业务逻辑层
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordUtil passwordUtil;

    /**
     * 分页查询所有用户
     */
    public Page<User> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return userRepository.findAll(pageable);
    }

    /**
     * 根据关键字搜索用户（分页）
     */
    public Page<User> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return userRepository.findByUsernameContainingOrNameContaining(keyword, keyword, pageable);
    }

    /**
     * 根据ID查找用户
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 保存用户（新增）- 自动加密密码
     */
    public User save(User user) {
        // 加密密码后存储
        user.setPassword(passwordUtil.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * 更新用户 - 如果密码不为空则加密后更新
     */
    public User update(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setRole(user.getRole());
        // 如果密码不为空则加密后更新
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordUtil.encode(user.getPassword()));
        }
        return userRepository.save(existingUser);
    }

    /**
     * 删除用户
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * 用户登录验证 - 使用 BCrypt 验证密码
     */
    public Optional<User> login(String username, String password) {
        logger.info("尝试登录 - 用户名: {}", username);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            logger.warn("用户不存在: {}", username);
            return Optional.empty();
        }
        
        User foundUser = user.get();
        logger.info("找到用户 - ID: {}, 用户名: {}, 密码哈希长度: {}", 
            foundUser.getId(), foundUser.getUsername(), 
            foundUser.getPassword() != null ? foundUser.getPassword().length() : 0);
        
        boolean matches = passwordUtil.matches(password, foundUser.getPassword());
        logger.info("密码验证结果: {}", matches);
        
        if (matches) {
            logger.info("登录成功: {}", username);
            return user;
        } else {
            logger.warn("密码错误 - 输入密码: {}, 数据库哈希: {}", 
                password, foundUser.getPassword());
        }
        return Optional.empty();
    }

    /**
     * 检查用户名是否已存在
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
