package com.xlj.tools.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author xlj
 * @date 2021/4/29
 */
@Controller
public class ProductController {
    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "xlj");
        return "hello";
    }
}
