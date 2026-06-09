package com.cheng.ming.mvc_user_manage.repository;

import com.cheng.ming.mvc_user_manage.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 标签数据访问层
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * 根据名称查找标签
     */
    Optional<Tag> findByName(String name);

    /**
     * 检查标签名是否已存在
     */
    boolean existsByName(String name);
}
