package com.xlj.tools.security;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * SpringSecurity Demo
 * - SpringSecurity 本质是一个过滤器链（有很多个过滤器）
 * - SpringBoot 项目自动配置 SpringSecurity 相关过滤器无需配置
     * - SSM + Shrio
     * - SpringBoot / SpringCloud + SpringSecurity
 * - 实现核心方式
     * - 实现 UserDetailService 接口，重写 loadUserByUsername 方法，获取自定义 User
     * - 继承 WebSecurityConfigurerAdapter ，重写 configure 方法
 * - 核心知识点
     * - 用户认证、授权、角色、注解使用、自定义页面（403、注销）
     * - 实现自动登录（十天内免登录）
 *
 * @author legend xu
 * @date 2022/7/15
 */
@Controller
@RequestMapping("/security")
public class SecurityController {
    @Secured({"ROLE_sale", "ROLE_boss"})     // 角色注解，访问改接口需要如下角色
    @ResponseBody
    @GetMapping("/role")
    public String role() {
        return "hello role";
    }

    @PreAuthorize("hasAnyAuthority('admin')")     // 权限注解，访问改接口需要如下权限
    @ResponseBody
    @GetMapping("/auth")
    public String auth() {
        return "hello auth";
    }


    /**
     * 页面跳转
     *
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "/security/login";
    }

    @GetMapping("/unauth")
    public String unauth() {
        return "/security/unauth";
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        return "hello SpringSecurity";
    }

    @GetMapping("/success")
    public String success() {
        return "/security/success";
    }

    @ResponseBody
    @GetMapping("/admin")
    public String admin() {
        return "hello admin";
    }

    @ResponseBody
    @GetMapping("/sale")
    public String sale() {
        return "hello sale";
    }
}
