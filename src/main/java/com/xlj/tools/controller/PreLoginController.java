package com.xlj.tools.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author xlj
 * @date 2021/6/11
 */
@Controller
public class PreLoginController {
    @GetMapping("/preLogin")
    public String preLogin() {
        return "preLogin";
    }
}
