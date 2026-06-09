package com.cheng.ming.mvc_user_manage.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 点赞记录（游客）
 */
@Entity
@Table(name = "likes")
public class LikeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文章ID
     */
    @Column(nullable = false)
    private Long articleId;

    /**
     * 访客IP地址（用于防止重复点赞）
     */
    @Column(length = 50, nullable = false)
    private String ipAddress;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
