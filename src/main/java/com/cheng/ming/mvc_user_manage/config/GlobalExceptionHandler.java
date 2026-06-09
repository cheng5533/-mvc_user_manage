package com.cheng.ming.mvc_user_manage.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理器
 * 统一处理所有未捕获的异常，返回友好的错误页面
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        // 记录错误日志
        logger.error("系统发生异常：", e);

        // 向页面传递错误信息
        model.addAttribute("errorMessage", "系统繁忙，请稍后重试");
        model.addAttribute("errorDetail", e.getMessage());

        // 返回错误页面
        return "error";
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e, Model model) {
        logger.error("运行时异常：", e);
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("errorDetail", e.getCause() != null ? e.getCause().getMessage() : "");
        return "error";
    }
}
