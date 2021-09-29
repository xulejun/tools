package com.xlj.tools.controller;

import com.xlj.tools.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * spring-session-redis 统一存储
 *
 * @author legend xu
 * @date 2021/9/29
 */
@Controller
public class LoginSessionController {

    @PostMapping("/doLogin")
    @ResponseBody
    public String loginSession(String username, HttpSession session) {
        User user = User.builder().id(1).name(username).build();
        session.setAttribute("loginUser", user);
        return "你好：" + user.getName();
    }

    @GetMapping("loginSession")
    public String loginSession() {
        return "loginSession";
    }
}
