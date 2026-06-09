package com.cheng.ming.mvc_user_manage.controller;

import com.cheng.ming.mvc_user_manage.entity.User;
import com.cheng.ming.mvc_user_manage.service.UserService;
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
 * 用户管理控制器
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 用户列表（分页）
     */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String keyword,
                       Model model, HttpSession session) {
        // 检查登录状态
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }

        Page<User> userPage;
        if (keyword != null && !keyword.isEmpty()) {
            userPage = userService.search(keyword, page, size);
            model.addAttribute("keyword", keyword);
        } else {
            userPage = userService.findAll(page, size);
        }

        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        return "user/list";
    }

    /**
     * 跳转到新增用户页面（仅管理员）
     */
    @GetMapping("/add")
    public String addForm(Model model, HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }
        
        // 检查是否为管理员
        User loginUser = (User) session.getAttribute("loginUser");
        if (!"管理员".equals(loginUser.getRole())) {
            logger.warn("非管理员尝试访问新增用户页面：{}", loginUser.getUsername());
            return "redirect:/user/list";
        }
        
        model.addAttribute("user", new User());
        model.addAttribute("isEdit", false);
        return "user/form";
    }

    /**
     * 保存新用户（仅管理员）
     */
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute User user,
                      BindingResult result, Model model,
                      RedirectAttributes redirectAttributes, HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }
        
        // 检查是否为管理员
        User loginUser = (User) session.getAttribute("loginUser");
        if (!"管理员".equals(loginUser.getRole())) {
            logger.warn("非管理员尝试新增用户：{}", loginUser.getUsername());
            return "redirect:/user/list";
        }

        // 检查用户名是否重复
        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "用户名已存在");
        }

        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "user/form";
        }

        userService.save(user);
        logger.info("管理员 {} 新增用户成功：{}", loginUser.getUsername(), user.getUsername());
        redirectAttributes.addFlashAttribute("message", "用户添加成功！");
        return "redirect:/user/list";
    }

    /**
     * 跳转到编辑用户页面
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }

        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        model.addAttribute("user", user);
        model.addAttribute("isEdit", true);
        return "user/form";
    }

    /**
     * 更新用户
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute User user,
                       BindingResult result, Model model,
                       RedirectAttributes redirectAttributes, HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "user/form";
        }

        userService.update(id, user);
        logger.info("更新用户成功，ID：{}", id);
        redirectAttributes.addFlashAttribute("message", "用户更新成功！");
        return "redirect:/user/list";
    }

    /**
     * 删除用户（仅管理员）
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes, HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }
        
        // 检查是否为管理员
        User loginUser = (User) session.getAttribute("loginUser");
        if (!"管理员".equals(loginUser.getRole())) {
            logger.warn("非管理员尝试删除用户，ID：{}，操作人：{}", id, loginUser.getUsername());
            return "redirect:/user/list";
        }

        userService.deleteById(id);
        logger.info("管理员 {} 删除用户成功，ID：{}", loginUser.getUsername(), id);
        redirectAttributes.addFlashAttribute("message", "用户删除成功！");
        return "redirect:/user/list";
    }
}
