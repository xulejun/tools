package com.xlj.tools.controller;

import com.xlj.tools.bean.User;
import com.xlj.tools.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XLJ
 * @date 2020/9/22
 */
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/insertOne")
    public void insertOne() {
        User user = User.builder().name("xlj").build();
        Integer integer = userService.insertUser(user);
        log.info(integer.toString());
    }
}
