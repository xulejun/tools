package com.xlj.tools.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

/**
 * spring-session-redis 统一存储
 *
 * @author legend xu
 * @date 2021/9/29
 */
@Controller
@RequestMapping("/session")
public class LoginSessionController {
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 处理登录请求
     *
     * @param username
     * @return
     */
    @PostMapping(value = "/doLogin")
    public String loginSession(String username, String password, @RequestParam("path") String path, HttpServletResponse response) {
        if (StrUtil.isNotBlank(username) && StrUtil.isNotBlank(password)) {
            String uuid = IdUtil.simpleUUID();
            redisTemplate.opsForValue().set(uuid, username);
            // 给当前登录服务器保留cookie，为了其他客户端在同一个浏览器下访问（不同domain）
            Cookie cookie = new Cookie("token", uuid);
            response.addCookie(cookie);
            return "redirect:" + path + "?token=" + uuid;
        }
        return "loginSession";
    }

    /**
     * 页面跳转，填写登录账号密码
     *
     * @return
     */
    @GetMapping("loginSession")
    public String loginSession(@RequestParam("path") String path, @CookieValue(value = "token", required = false) String token, Model model) {
        // 同一个浏览器不同domain下访问，之前已经访问的情况下，则不需要再访问
        if (StrUtil.isNotBlank(token)) {
            return "redirect:" + path + "?token=" + token;
        }
        model.addAttribute("path", path);
        return "/session/login";
    }

    /**
     * 登录后获取员工列表
     *
     * @param model
     * @param httpSession
     * @return
     */
    @GetMapping("employees")
    public String employees(Model model, HttpSession httpSession,
                            @RequestParam(value = "token", required = false) String token) {
        if (StrUtil.isNotBlank(token)) {
            String username = redisTemplate.opsForValue().get(token);
            // 登录成功后，将会话保存下来
            httpSession.setAttribute("loginUser", username);
        }

        if (Objects.isNull(httpSession.getAttribute("loginUser"))) {
            // 将自身url通过参数进行传递，后续流程实现跳转
            return "redirect:http://127.0.0.1:8080/session/loginSession" + "?path=employees";
        } else {
            List<String> list = Lists.newArrayList();
            list.add("张三");
            list.add("李四");
            model.addAttribute("list", list);
            return "/session/listEmployees";
        }
    }
}
