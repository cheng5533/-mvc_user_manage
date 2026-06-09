package com.cheng.ming.mvc_user_manage.controller;

import com.cheng.ming.mvc_user_manage.entity.Category;
import com.cheng.ming.mvc_user_manage.entity.User;
import com.cheng.ming.mvc_user_manage.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 后台分类管理控制器
 */
@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类列表
     */
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }
        User loginUser = (User) session.getAttribute("loginUser");
        if (!"管理员".equals(loginUser.getRole())) {
            return "redirect:/blog";
        }

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("category", new Category());
        return "blog/admin/categories";
    }

    /**
     * 新增分类
     */
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute Category category,
                      BindingResult result,
                      RedirectAttributes redirectAttributes,
                      HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }

        if (categoryService.existsByName(category.getName())) {
            redirectAttributes.addFlashAttribute("error", "分类名称已存在！");
            return "redirect:/admin/category/list";
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "请检查输入");
            return "redirect:/admin/category/list";
        }

        categoryService.save(category);
        redirectAttributes.addFlashAttribute("message", "分类添加成功！");
        return "redirect:/admin/category/list";
    }

    /**
     * 删除分类
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes,
                         HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }

        categoryService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "分类已删除！");
        return "redirect:/admin/category/list";
    }
}
