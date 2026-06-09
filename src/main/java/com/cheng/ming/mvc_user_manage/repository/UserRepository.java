package com.cheng.ming.mvc_user_manage.repository;

import com.cheng.ming.mvc_user_manage.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据用户名或姓名模糊查询（分页）
     */
    Page<User> findByUsernameContainingOrNameContaining(String username, String name, Pageable pageable);

    /**
     * 检查用户名是否已存在
     */
    boolean existsByUsername(String username);
}
