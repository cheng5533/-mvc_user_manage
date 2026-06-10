package com.cheng.ming.mvc_user_manage.service;

import com.cheng.ming.mvc_user_manage.entity.User;
import com.cheng.ming.mvc_user_manage.repository.UserRepository;
import com.cheng.ming.mvc_user_manage.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户业务逻辑层
 * 修复版本: 移除错误的日志代码,恢复原始正常工作代码
 */
@Service
public class UserService {

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
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordUtil.matches(password, user.get().getPassword())) {
            return user;
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
