package com.xlj.tools.controller;

import com.xlj.tools.bean.Result;
import com.xlj.tools.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * element集成
 *
 * @author xlj
 * @date 2021/5/6
 */
@Slf4j
@Controller
public class ElementController {
    @GetMapping("/element")
    public String ajaxRequest() {
        return "element";
    }

    @ResponseBody
    @GetMapping("/getUser")
    public Result<?> getUser() {
        User user = User.builder().id(1).name("xlj").build();
        return Result.success(user);
    }
}
