package com.cheng.ming.mvc_user_manage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * 评论实体类（支持游客评论，无需登录）
 */
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article; // 所属文章

    @Column(name = "parent_id")
    private Long parentId = 0L; // 父评论ID（0表示一级评论）

    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    @Column(nullable = false)
    private String nickname; // 评论者昵称（游客填写）

    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email; // 邮箱（选填，用于接收回复通知）

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 评论内容

    @Column(name = "ip_address", length = 50)
    private String ipAddress; // 评论者IP地址

    @Column(nullable = false)
    private Integer status = 1; // 状态：1-显示，0-隐藏（审核用）

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
        if (this.parentId == null) {
            this.parentId = 0L;
        }
        if (this.status == null) {
            this.status = 1;
        }
    }

    @PreUpdate
    public void preUpdate() {
        // 更新时保留原有的 create_time,只更新 updateTime(如果有)
        // 确保 create_time 不会被设置为 null
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now();
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
