package com.xlj.tools.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xlj.tools.bean.PreLogin;
import com.xlj.tools.service.SpiderContentService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.xlj.tools.util.SeleniumUtil.*;

/**
 * 通过预登陆配置采集文章
 *
 * @author xlj
 * @date 2021/6/15
 */
@Slf4j
@Service
public class SpiderContentServiceImpl implements SpiderContentService {
    @Override
    public String content(PreLogin preLogin) {
        // 模拟登录
        String content = null;
        try {
            String cookies = mockLogin(preLogin);
            content = staticParse(cookies, preLogin.getArticleUrl());
        } catch (Exception e) {
            log.warn("模拟登录异常：{}", e);
        }
        return content;
    }

    /**
     * HttpRequest静态采集
     *
     * @param cookies
     */
    private String staticParse(String cookies, String articleUrl) {
        HttpResponse response = HttpRequest.get(articleUrl)
                .header("cookie", cookies)
                .execute();
        return response.body();
    }

    /**
     * 通过界面配置实现模拟登录
     *
     * @param preLogin
     * @return
     * @throws Exception
     */
    private String mockLogin(PreLogin preLogin) throws Exception {
        getBrowser();
        browser.get(preLogin.getLoginUrl());
        // 等待直到登录的xpath出现
        new WebDriverWait(browser, 100, 1000)
                .until(ExpectedConditions.visibilityOf(browser.findElement(By.xpath(preLogin.getLoginXpath()))));
        // Xpath校验
        String suffix = "/..";
        WebElement userNameElement = browser.findElementByXPath(preLogin.getUserNameXpath() + suffix);
        if (!userNameElement.getText().contains("用户名") && !userNameElement.getText().contains("账号")) {
//            log.warn("账号Xpath有误");
            throw new Exception("账号Xpath有误");
        }
        WebElement passwordElement = browser.findElementByXPath(preLogin.getPasswordXpath() + suffix);
        if (!passwordElement.getText().contains("密码")) {
//            log.warn("密码Xpath有误");
            throw new Exception("账号Xpath有误");
        }
        WebElement loginElement = browser.findElementByXPath(preLogin.getLoginXpath());
        if (!loginElement.getText().contains("登录")) {
//            log.warn("登录Xpath有误");
            throw new Exception("账号Xpath有误");
        }
        // 输入账号密码登录
        browser.findElementByXPath(preLogin.getUserNameXpath()).sendKeys(preLogin.getUserName());
        browser.findElementByXPath(preLogin.getPasswordXpath()).sendKeys(preLogin.getPassword());
        browser.findElementByXPath(preLogin.getLoginXpath()).click();
        // 等待页面登录响应，todo 待完善
        TimeUnit.SECONDS.sleep(5);
        // 获取cookies
        StringBuilder cookies = new StringBuilder();
        browser.manage().getCookies().forEach(cookie -> {
            cookies.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        });
        log.info("获取到的cookies：{}", cookies);
        browser.quit();
        return cookies.toString();
    }
}
