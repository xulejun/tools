package com.xlj.tools.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xlj.tools.bean.Product;
import com.xlj.tools.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/listProduct")
    public String listProduct(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                              @RequestParam(value = "size", defaultValue = "5") int size) {
        // 分页
        PageHelper.startPage(start, size);
        List<Product> productList = productService.selectAll();
        PageInfo<Product> page = new PageInfo<>(productList);
        model.addAttribute("page", page);
        return "listProduct";
    }

    @PostMapping("/addProduct")
    public String addProduct(Product product) {
        productService.insert(product);
        return "redirect:listProduct";
    }
}
