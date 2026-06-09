package com.cheng.ming.mvc_user_manage.service;

import com.cheng.ming.mvc_user_manage.entity.LikeRecord;
import com.cheng.ming.mvc_user_manage.repository.LikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 点赞服务（游客）
 */
@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    /**
     * 点赞（防止重复）
     * @return true=点赞成功, false=已点过赞
     */
    @Transactional
    public boolean toggleLike(Long articleId, String ipAddress) {
        // 检查是否已点赞
        if (likeRepository.existsByArticleIdAndIpAddress(articleId, ipAddress)) {
            return false; // 已点过，不重复点赞
        }

        // 创建新点赞记录
        LikeRecord like = new LikeRecord();
        like.setArticleId(articleId);
        like.setIpAddress(ipAddress);
        likeRepository.save(like);
        return true;
    }

    /**
     * 获取文章点赞数
     */
    public long getLikeCount(Long articleId) {
        return likeRepository.countByArticleId(articleId);
    }

    /**
     * 检查某IP是否已点赞
     */
    public boolean hasLiked(Long articleId, String ipAddress) {
        return likeRepository.existsByArticleIdAndIpAddress(articleId, ipAddress);
    }
}
