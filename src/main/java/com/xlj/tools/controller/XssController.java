package com.xlj.tools.controller;

import com.xlj.tools.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

/**
 * XSS过滤器-控制层
 *
 * @author xlj
 * @date 2021/7/7
 */
@Slf4j
@RestController
public class XssController {
    /**
     * 键值对方式
     *
     * @param id
     * @param name
     * @return
     */
    @PostMapping("/xssFilter")
    public String xssFilter(Integer id, String name) {
        return MessageFormat.format("id:{0}, name:{1}", id, name);
    }

    /**
     * 实体类
     * postman：Body——》raw——》
     {
          "id":"1",
          "name":"<script>alert(1);</script>"
     }

     * @param user
     * @return
     */
    @PostMapping("/modelXssFilter")
    public User modelXssFilter(@RequestBody User user) {
        return user;
    }

    /**
     * 不转义
     *
     * @param name
     * @return
     */
    @PostMapping("/open/xssFilter")
    public String openXssFilter(String name) {
        return name;
    }
}
