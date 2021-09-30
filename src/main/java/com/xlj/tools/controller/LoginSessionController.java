package com.xlj.tools.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
     * @param session
     * @return
     */
    @PostMapping(value = "/doLogin")
    public String loginSession(String username, String password, @RequestParam("path") String path, HttpSession session) {
        if (StrUtil.isNotBlank(username) && StrUtil.isNotBlank(password)) {
            String uuid = IdUtil.simpleUUID();
            redisTemplate.opsForValue().set(uuid, username);
            return "redirect:" + path + "?token=" + uuid;
        }
        return "loginSession";
    }

    /**
     * 页面跳转
     *
     * @return
     */
    @GetMapping("loginSession")
    public String loginSession(@RequestParam("path") String path, Model model) {
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
            httpSession.setAttribute("loginUser", token);
        }

        if (Objects.isNull(httpSession.getAttribute("loginUser"))) {
            // 将自身url通过参数进行传递，后续流程实现跳转
            return "redirect:http://127.0.0.1:8080/session/loginSession" + "?path=employees";
        } else {
//            String username = redisTemplate.opsForValue().get(token);
            List<String> list = Lists.newArrayList();
            list.add("张三");
            list.add("李四");
            model.addAttribute("list", list);
            model.addAttribute("username", "xlj");
            return "/session/listEmployees";
        }
    }
}
