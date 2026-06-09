package com.cheng.ming.mvc_user_manage.controller;

import com.cheng.ming.mvc_user_manage.entity.Article;
import com.cheng.ming.mvc_user_manage.entity.Comment;
import com.cheng.ming.mvc_user_manage.service.ArticleService;
import com.cheng.ming.mvc_user_manage.service.CategoryService;
import com.cheng.ming.mvc_user_manage.service.CommentService;
import com.cheng.ming.mvc_user_manage.service.LikeService;
import com.cheng.ming.mvc_user_manage.service.TagService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 博客前台控制器（面向所有访客，无需登录）
 */
@Controller
public class BlogController {

    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private LikeService likeService;

    /**
     * 博客首页（文章列表）
     */
    @GetMapping("/blog")
    public String index(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "6") int size,
                        @RequestParam(required = false) Long categoryId,
                        @RequestParam(required = false) String keyword,
                        Model model) {
        Page<Article> articlePage;
        if (keyword != null && !keyword.isEmpty()) {
            articlePage = articleService.search(keyword, page, size);
            model.addAttribute("keyword", keyword);
        } else if (categoryId != null) {
            articlePage = articleService.findByCategory(categoryId, page, size);
            model.addAttribute("categoryId", categoryId);
        } else {
            articlePage = articleService.findPublishedArticles(page, size);
        }

        model.addAttribute("articlePage", articlePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("tags", tagService.findAll());
        model.addAttribute("latestArticles", articleService.findLatestArticles());
        return "blog/index";
    }

    /**
     * 文章详情页
     */
    @GetMapping("/blog/article/{id}")
    public String articleDetail(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id)
                .orElseThrow(() -> new RuntimeException("文章不存在"));

        // 增加阅读量
        articleService.incrementViewCount(id);

        // 获取评论
        model.addAttribute("article", article);
        model.addAttribute("comments", commentService.findByArticle(article));
        model.addAttribute("commentCount", commentService.countByArticle(article));
        model.addAttribute("comment", new Comment());
        return "blog/article";
    }

    /**
     * 提交评论（游客，无需登录）
     */
    @PostMapping("/blog/article/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @Valid @ModelAttribute Comment comment,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentError", "请填写昵称和评论内容");
            return "redirect:/blog/article/" + id;
        }

        Article article = articleService.findById(id)
                .orElseThrow(() -> new RuntimeException("文章不存在"));

        comment.setArticle(article);
        // 获取访客IP
        String ip = request.getRemoteAddr();
        comment.setIpAddress(ip);

        commentService.save(comment);
        redirectAttributes.addFlashAttribute("commentSuccess", "评论发表成功！");
        return "redirect:/blog/article/" + id;
    }

    /**
     * 游客发布文章页面
     */
    @GetMapping("/blog/write")
    public String writeArticleForm(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("categories", categoryService.findAll());
        return "blog/write";
    }

    /**
     * 游客提交文章
     */
    @PostMapping("/blog/write")
    public String submitArticle(@Valid @ModelAttribute Article article,
                                BindingResult result,
                                @RequestParam(required = false) Long categoryId,
                                @RequestParam(required = false) String tagNames,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("writeError", "请填写标题和内容");
            return "redirect:/blog/write";
        }

        try {
            articleService.save(article, categoryId, tagNames);
            redirectAttributes.addFlashAttribute("writeSuccess", "文章发布成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("writeError", "发布失败：" + e.getMessage());
            return "redirect:/blog/write";
        }
        return "redirect:/blog";
    }

    /**
     * 点赞（游客）
     */
    @PostMapping("/blog/article/{id}/like")
    @ResponseBody
    public java.util.Map<String, Object> likeArticle(@PathVariable Long id, HttpServletRequest request) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        String ipAddress = request.getRemoteAddr();

        boolean success = likeService.toggleLike(id, ipAddress);
        long likeCount = likeService.getLikeCount(id);

        if (success) {
            result.put("success", true);
            result.put("message", "点赞成功");
            result.put("likeCount", likeCount);
        } else {
            result.put("success", false);
            result.put("message", "已经点过赞了");
            result.put("likeCount", likeCount);
        }
        return result;
    }

    /**
     * 获取点赞数
     */
    @GetMapping("/blog/article/{id}/like-count")
    @ResponseBody
    public java.util.Map<String, Object> getLikeCount(@PathVariable Long id) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("likeCount", likeService.getLikeCount(id));
        return result;
    }
}
