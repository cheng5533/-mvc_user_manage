package com.cheng.ming.mvc_user_manage.repository;

import com.cheng.ming.mvc_user_manage.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论数据访问层
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 查询某篇文章的所有评论（按时间升序）
     */
    List<Comment> findByArticleAndStatusOrderByCreateTimeAsc(
        com.cheng.ming.mvc_user_manage.entity.Article article, 
        Integer status
    );

    /**
     * 查询某条评论的子评论
     */
    List<Comment> findByParentIdAndStatusOrderByCreateTimeAsc(Long parentId, Integer status);

    /**
     * 统计某篇文章的评论数
     */
    long countByArticleAndStatus(
        com.cheng.ming.mvc_user_manage.entity.Article article, 
        Integer status
    );
}
