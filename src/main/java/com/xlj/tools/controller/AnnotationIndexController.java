package com.xlj.tools.controller;

import com.xlj.tools.annotation.LoginRequired;
import com.xlj.tools.annotation.MyLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义注解登录拦截Controller
 *
 * @author xlj
 * @date 2021/4/29
 */
@RestController
public class AnnotationIndexController {

    @GetMapping("/indexA")
    public String indexA() {
        return "你正在访问A页面";
    }

    @LoginRequired  // 加入登录拦截器
    @GetMapping("/indexB")
    public String indexB() {
        return "你正在访问B页面";
    }

    @MyLog
    @GetMapping("/indexC/{sourceName}")
    public String sourceC(@PathVariable(value = "sourceName") String sourceName) {
        return "你正在访问sourceC资源";
    }
}
