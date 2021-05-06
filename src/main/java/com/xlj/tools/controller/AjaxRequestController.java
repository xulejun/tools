package com.xlj.tools.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.xlj.tools.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * AJAX请求数据
 *
 * @author xlj
 * @date 2021/5/6
 */
@Slf4j
@Controller
public class AjaxRequestController {
    @GetMapping("/ajaxRequest")
    public String ajaxRequest() {
        return "ajaxRequest";
    }

    /**
     * ajax 请求获取单个对象数据
     *
     * @return
     */
    @GetMapping("/getOneUser")
    @ResponseBody
    public String getOneUser() {
        User user = User.builder().id(1).name("XLJ").build();
        JSONObject jsonObject = new JSONObject(user);
        return jsonObject.toString();
    }

    /**
     * ajax 请求获取多个对象数据
     *
     * @return
     */
    @GetMapping("/getManyUser")
    @ResponseBody
    public String getManyUser() {
        List<User> list = new ArrayList<>(5);
        for (int i = 1; i < 6; i++) {
            User user = User.builder().id(i).name("XLJ-" + i).build();
            list.add(user);
        }
        return new JSONArray(list).toString();
    }

    /**
     * ajax 前端提交数据到后端
     *
     * @return
     */
    @PostMapping("/submitUser")
    public String submitUser(@RequestBody User user) {
        log.info("前端传过来的用户ID：{}", user.getId());
        log.info("前端传过来的用户名：{}", user.getName());
        return "redirect:ajaxRequest";
    }
}
