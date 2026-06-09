package com.cheng.ming.mvc_user_manage.repository;

import com.cheng.ming.mvc_user_manage.entity.LikeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 点赞记录Repository
 */
@Repository
public interface LikeRepository extends JpaRepository<LikeRecord, Long> {

    /**
     * 查询某文章是否已被某IP点赞
     */
    boolean existsByArticleIdAndIpAddress(Long articleId, String ipAddress);

    /**
     * 统计某文章的点赞数
     */
    long countByArticleId(Long articleId);

    /**
     * 删除某文章的所有点赞记录（管理员功能）
     */
    void deleteByArticleId(Long articleId);
}
