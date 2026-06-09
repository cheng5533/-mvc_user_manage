package com.cheng.ming.mvc_user_manage.controller;

import com.cheng.ming.mvc_user_manage.entity.Article;
import com.cheng.ming.mvc_user_manage.entity.User;
import com.cheng.ming.mvc_user_manage.service.ArticleService;
import com.cheng.ming.mvc_user_manage.service.CategoryService;
import com.cheng.ming.mvc_user_manage.service.TagService;
import jakarta.servlet.http.HttpSession;
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
 * 后台文章管理控制器（需要管理员登录）
 */
@Controller
@RequestMapping("/admin/article")
public class AdminArticleController {

    private static final Logger logger = LoggerFactory.getLogger(AdminArticleController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    /**
     * 检查管理员权限
     */
    private boolean checkAdmin(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return false;
        }
        if (!"管理员".equals(loginUser.getRole())) {
            return false;
        }
        return true;
    }

    /**
     * 文章列表（后台）
     */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model, HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }
        if (!checkAdmin(session, model)) {
            return "redirect:/blog";
        }

        Page<Article> articlePage = articleService.findAll(page, size);
        model.addAttribute("articlePage", articlePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());
        model.addAttribute("totalItems", articlePage.getTotalElements());
        return "blog/admin/list";
    }

    /**
     * 新增文章页面
     */
    @GetMapping("/add")
    public String addForm(Model model, HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }
        if (!checkAdmin(session, model)) {
            return "redirect:/blog";
        }

        model.addAttribute("article", new Article());
        model.addAttribute("isEdit", false);
        model.addAttribute("categories", categoryService.findAll());
        return "blog/admin/form";
    }

    /**
     * 保存文章
     */
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute Article article,
                      BindingResult result,
                      @RequestParam(required = false) Long categoryId,
                      @RequestParam(required = false) String tagNames,
                      Model model,
                      RedirectAttributes redirectAttributes,
                      HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }
        User loginUser = (User) session.getAttribute("loginUser");
        if (!"管理员".equals(loginUser.getRole())) {
            return "redirect:/blog";
        }

        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            model.addAttribute("categories", categoryService.findAll());
            return "blog/admin/form";
        }

        articleService.save(article, categoryId, tagNames);
        redirectAttributes.addFlashAttribute("message", "文章发布成功！");
        return "redirect:/admin/article/list";
    }

    /**
     * 编辑文章页面
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }
        if (!checkAdmin(session, model)) {
            return "redirect:/blog";
        }

        Article article = articleService.findById(id)
                .orElseThrow(() -> new RuntimeException("文章不存在"));
        model.addAttribute("article", article);
        model.addAttribute("isEdit", true);
        model.addAttribute("categories", categoryService.findAll());

        // 将当前标签转为逗号分隔字符串
        if (article.getTags() != null && !article.getTags().isEmpty()) {
            String tagStr = article.getTags().stream()
                    .map(t -> t.getName())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            model.addAttribute("tagNames", tagStr);
        }

        return "blog/admin/form";
    }

    /**
     * 更新文章
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute Article article,
                       BindingResult result,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(required = false) String tagNames,
                       Model model,
                       RedirectAttributes redirectAttributes,
                       HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }
        if (!checkAdmin(session, model)) {
            return "redirect:/blog";
        }

        if (result.hasErrors()) {
            model.addAttribute("isEdit", true);
            model.addAttribute("categories", categoryService.findAll());
            return "blog/admin/form";
        }

        articleService.update(id, article, categoryId, tagNames);
        redirectAttributes.addFlashAttribute("message", "文章更新成功！");
        return "redirect:/admin/article/list";
    }

    /**
     * 删除文章
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes,
                         HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }
        if (!checkAdmin(session, null)) {
            return "redirect:/blog";
        }

        articleService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "文章已删除！");
        return "redirect:/admin/article/list";
    }
}
