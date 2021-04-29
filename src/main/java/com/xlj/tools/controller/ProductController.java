package com.xlj.tools.controller;

import com.xlj.tools.bean.Product;
import com.xlj.tools.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author xlj
 * @date 2021/4/29
 */
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "xlj");
        return "hello";
    }

    @GetMapping("/list")
    public String listProduct(Model model) {
        List<Product> productList = productService.selectAll();
        model.addAttribute("ps", productList);
        return "listProduct";
    }


}
