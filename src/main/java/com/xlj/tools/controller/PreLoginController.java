package com.xlj.tools.controller;

import com.xlj.tools.bean.PreLogin;
import com.xlj.tools.service.SpiderContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 预登陆采集
 *
 * @author xlj
 * @date 2021/6/11
 */
@Controller
public class PreLoginController {
    @Autowired
    private SpiderContentService spiderContentService;

    @GetMapping("/preLogin")
    public String preLogin() {
        return "preLogin";
    }

    @GetMapping("/preLoginPage")
    public String preLoginPage() {
        return "preLoginPage";
    }

    @PostMapping("/collect")
    public ModelAndView collect(ModelAndView model, PreLogin preLogin) {
        // 传递登录配置表单采集文章
        String content = spiderContentService.content(preLogin);
        model.addObject("content", content);
        model.setViewName("/spiderContent");
        return model;
    }

}
