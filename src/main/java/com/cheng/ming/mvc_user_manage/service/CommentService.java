package com.cheng.ming.mvc_user_manage.service;

import com.cheng.ming.mvc_user_manage.entity.Article;
import com.cheng.ming.mvc_user_manage.entity.Comment;
import com.cheng.ming.mvc_user_manage.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评论业务逻辑层
 */
@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentRepository commentRepository;

    /**
     * 获取文章的所有评论
     */
    public List<Comment> findByArticle(Article article) {
        return commentRepository.findByArticleAndStatusOrderByCreateTimeAsc(article, 1);
    }

    /**
     * 统计文章评论数
     */
    public long countByArticle(Article article) {
        return commentRepository.countByArticleAndStatus(article, 1);
    }

    /**
     * 保存评论（游客评论，无需登录）
     */
    @Transactional
    public Comment save(Comment comment) {
        logger.info("新评论：文章ID={}, 昵称={}", comment.getArticle().getId(), comment.getNickname());
        return commentRepository.save(comment);
    }

    /**
     * 删除评论（管理员操作）
     */
    @Transactional
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
        logger.info("删除评论，ID：{}", id);
    }

    /**
     * 隐藏评论（审核用）
     */
    @Transactional
    public void hide(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("评论不存在"));
        comment.setStatus(0);
        commentRepository.save(comment);
    }
}
