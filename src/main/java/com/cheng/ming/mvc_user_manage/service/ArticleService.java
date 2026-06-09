package com.cheng.ming.mvc_user_manage.service;

import com.cheng.ming.mvc_user_manage.entity.Article;
import com.cheng.ming.mvc_user_manage.entity.Category;
import com.cheng.ming.mvc_user_manage.entity.Tag;
import com.cheng.ming.mvc_user_manage.entity.User;
import com.cheng.ming.mvc_user_manage.repository.ArticleRepository;
import com.cheng.ming.mvc_user_manage.repository.CategoryRepository;
import com.cheng.ming.mvc_user_manage.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 文章业务逻辑层
 */
@Service
public class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    /**
     * 分页查询已发布的文章
     */
    public Page<Article> findPublishedArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleRepository.findByStatusOrderByCreateTimeDesc(1, pageable);
    }

    /**
     * 根据分类查询文章
     */
    public Page<Article> findByCategory(Long categoryId, int page, int size) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return Page.empty();
        }
        Pageable pageable = PageRequest.of(page, size);
        return articleRepository.findByCategoryAndStatusOrderByCreateTimeDesc(category, 1, pageable);
    }

    /**
     * 搜索文章
     */
    public Page<Article> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleRepository.search(keyword, pageable);
    }

    /**
     * 获取文章详情（增加阅读量）
     */
    @Transactional
    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    /**
     * 增加阅读量
     */
    @Transactional
    public void incrementViewCount(Long id) {
        articleRepository.incrementViewCount(id);
    }

    /**
     * 获取最新文章（侧边栏用）
     */
    public List<Article> findLatestArticles() {
        return articleRepository.findTop5ByStatusOrderByCreateTimeDesc(1);
    }

    /**
     * 保存文章（新增）- 支持游客发布
     */
    @Transactional
    public Article save(Article article, Long categoryId, String tagNames) {
        // 设置分类
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            article.setCategory(category);
        }
        // 处理标签
        if (tagNames != null && !tagNames.trim().isEmpty()) {
            Set<Tag> tags = processTags(tagNames);
            article.setTags(tags);
        }
        // 游客发布，author为null
        article.setAuthor(null);
        logger.info("新增文章：{}", article.getTitle());
        return articleRepository.save(article);
    }

    /**
     * 更新文章
     */
    @Transactional
    public Article update(Long id, Article article, Long categoryId, String tagNames) {
        Article existing = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文章不存在"));
        existing.setTitle(article.getTitle());
        existing.setContent(article.getContent());
        existing.setCoverImage(article.getCoverImage());
        existing.setStatus(article.getStatus());

        // 更新分类
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            existing.setCategory(category);
        }

        // 更新标签
        if (tagNames != null) {
            Set<Tag> tags = processTags(tagNames);
            existing.setTags(tags);
        }

        logger.info("更新文章：{}", existing.getTitle());
        return articleRepository.save(existing);
    }

    /**
     * 删除文章
     */
    @Transactional
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
        logger.info("删除文章，ID：{}", id);
    }

    /**
     * 查询所有文章（后台管理用，包含草稿）
     */
    public Page<Article> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleRepository.findAll(pageable);
    }

    /**
     * 处理标签字符串（逗号分隔），不存在则自动创建
     */
    private Set<Tag> processTags(String tagNames) {
        Set<Tag> tags = new HashSet<>();
        String[] names = tagNames.split("[,，]");
        for (String name : names) {
            String trimmed = name.trim();
            if (trimmed.isEmpty()) continue;
            Tag tag = tagRepository.findByName(trimmed)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(trimmed);
                        return tagRepository.save(newTag);
                    });
            tags.add(tag);
        }
        return tags;
    }
}
