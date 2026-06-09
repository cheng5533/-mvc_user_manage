package com.cheng.ming.mvc_user_manage.controller;

import com.cheng.ming.mvc_user_manage.entity.User;
import com.cheng.ming.mvc_user_manage.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 登录控制器
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    /**
     * 显示登录页面
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        // 如果已登录，重定向到列表页
        model.addAttribute("user", new User());
        return "login";
    }

    /**
     * 显示注册页面
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * 处理注册请求
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute User user,
                           BindingResult result, Model model,
                           RedirectAttributes redirectAttributes) {
        // 检查用户名是否重复
        if (userService.existsByUsername(user.getUsername())) {
            logger.warn("注册失败，用户名已存在：{}", user.getUsername());
            result.rejectValue("username", "error.user", "用户名已存在");
        }

        if (result.hasErrors()) {
            return "register";
        }

        // 默认角色为普通用户
        user.setRole("普通用户");
        userService.save(user);
        logger.info("用户注册成功：{}", user.getUsername());
        redirectAttributes.addFlashAttribute("message", "注册成功！请登录");
        return "redirect:/login";
    }

    /**
     * 处理登录请求
     */
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model, HttpSession session) {
        Optional<User> user = userService.login(username, password);
        if (user.isPresent()) {
            session.setAttribute("loginUser", user.get());
            logger.info("用户登录成功：{}", username);
            return "redirect:/blog";
        }
        logger.warn("用户登录失败：{}", username);
        model.addAttribute("error", "用户名或密码错误");
        return "login";
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null) {
            logger.info("用户退出登录：{}", loginUser.getUsername());
        }
        session.invalidate();
        return "redirect:/login";
    }

    /**
     * 首页重定向到博客
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/blog";
    }
}
