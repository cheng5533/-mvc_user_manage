package com.cheng.ming.mvc_user_manage.repository;

import com.cheng.ming.mvc_user_manage.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章数据访问层
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * 分页查询已发布的文章（按创建时间降序）
     */
    Page<Article> findByStatusOrderByCreateTimeDesc(Integer status, Pageable pageable);

    /**
     * 根据分类查询文章
     */
    Page<Article> findByCategoryAndStatusOrderByCreateTimeDesc(
        com.cheng.ming.mvc_user_manage.entity.Category category, 
        Integer status, 
        Pageable pageable
    );

    /**
     * 根据标题或内容模糊搜索
     */
    @Query("SELECT a FROM Article a WHERE a.status = 1 AND (a.title LIKE %:keyword% OR a.content LIKE %:keyword%) ORDER BY a.createTime DESC")
    Page<Article> search(String keyword, Pageable pageable);

    /**
     * 增加阅读量
     */
    @Modifying
    @Query("UPDATE Article a SET a.viewCount = a.viewCount + 1 WHERE a.id = :id")
    void incrementViewCount(Long id);

    /**
     * 查询最新文章（用于侧边栏）
     */
    List<Article> findTop5ByStatusOrderByCreateTimeDesc(Integer status);
}
