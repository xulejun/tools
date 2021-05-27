package com.xlj.tools;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * selenium整合测试，预登陆
 *
 * @author xlj
 * @date 2021/5/25
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ToolsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeleniumTest {
    static String cookies = "__utmc=118792214; ASP.NET_SessionId=u2zv51by5x5odhuj4j1n3s55; ASPSESSIONIDSGCSTBAT=NMOGBIPAECGFNDBALEOMAOEJ; ASPSESSIONIDSGCQQACR=EKIOPDMBDNMDCPKBJMMACDOC; ASPSESSIONIDQEARTCDQ=HPKINPICMDDJHPOBHLBLJHEL; Hm_lvt_4df7897ce5ff84810a1a7e9a1ace8249=1621818337,1621923874,1622015315; __utmz=118792214.1622093871.4.4.utmcsr=login.lgmi.com|utmccn=(referral)|utmcmd=referral|utmcct=/; ASPSESSIONIDQEASQBBT=JGODMLFDCPEJNJJGGIBAHNDA; __utma=118792214.842640809.1622015328.1622093871.1622102334.5; __utmt=1; __utmt_~1=1; Hm_lpvt_4df7897ce5ff84810a1a7e9a1ace8249=1622102674; __utmb=118792214.11.10.1622102334; lgmi=ClientID=badboy&userprivilege=infotest&Mytime=2021/5/27 16:04:34";
    // 卓创登录url
//        String loginUrl = "https://www.sci99.com/";
//        String loginUrl = "https://www.sci99.com/include/scilogin.aspx";
//        String articleUrl = "https://steel.sci99.com/news/38363817.html";
    // 兰格
//        String loginUrl = "https://info.lgmi.com/login_news/login.aspx";
    String loginUrl = "https://www.lgmi.com/";
    String articleUrl1 = "https://jiancai.lgmi.com/html.aspx?cityid=&articleid=A1102&recordno=231283&u=/huizong/2021/05/24/A1102_a0f951.htm";
    String articleUrl2 = "https://guancai.lgmi.com/html.aspx?cityid=A0328&articleid=A1501&recordno=5583511&u=/hangqing/2021/05/27/A0328_A1501.htm";
    // 简数
//        String loginUrl = "http://account.keydatas.com/login";
//        String articleUrl = "http://dash.keydatas.com/task/group/list";
    // qq邮箱
//        String loginUrl = "https://mail.qq.com";
//        String articleUrl = "https://mail.qq.com/cgi-bin/setting4?fun=list&acc=1&sid=NWkP8VOop7kUNnIS&go=mybirthinfo";
    private static ChromeDriver browser;

    // 驱动路径（在resource/driver下有该驱动）
    @Value("${selenium.webDriverPath}")
    private String driverPath;


    @Test
    public void preLoginTest() throws Exception {
//        getBrowser();

        // 持续等待，直到扫码完成（扫码登录上去cookies的size=13）
//        long waitTime = 1L;
//        int waitCount = 1;
//        while (browser.manage().getCookies().size() < 2) {
//            TimeUnit.SECONDS.sleep(waitTime);
//            log.info("等待时间：{} 秒", waitTime * waitCount);
//            waitCount++;
//        }
//        browser.get(loginUrl);
//        TimeUnit.SECONDS.sleep(20L);

        // 获取cookies
//        StringBuilder cookies = new StringBuilder();
//        browser.manage().getCookies().forEach(cookie -> {
//            cookies.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
//        });
//        log.info("获取到的cookies：{}", cookies);

//        JSONArray jsonArray = new JSONArray(jsonStr);
//        List<Cookie> cookieList = jsonArray.stream().map(ii -> {
//            JSONObject it = (JSONObject) ii;
//            return new Cookie(
//                    it.getStr("name"),
//                    it.getStr("value"),
//                    it.getStr("domain"),
//                    it.getStr("path"),
//                    it.getDate("expiry"),
//                    it.getBool("isSecure"),
//                    it.getBool("isHttpOnly"));
//        }).collect(Collectors.toList());
//        List<Cookie> cookieList = new ArrayList<>(browser.manage().getCookies());
//        log.info("获取到的cookies：{}", cookieList);
//        browser.quit();


        getBrowser();
        // 先访问，不然添加cookie会报错:invalid cookie domain
        browser.get(loginUrl);
//        browser.manage().deleteAllCookies();

        // 添加cookie
//        for (Cookie cookie : cookieList) {
//            browser.manage().addCookie(cookie);
//        }
        List<String> cookieList = StrUtil.splitTrim(cookies, ";");
        for (String cookie : cookieList) {
            String cookieName = cookie.substring(0, cookie.indexOf("="));
            String cookieValue = cookie.substring(cookie.indexOf("=") + 1);
            browser.manage().addCookie(new Cookie(cookieName, cookieValue, ".lgmi.com", "/", null));
        }

        // 文章请求
//        HttpResponse response1 = HttpRequest.get(articleUrl1)
//                .header("cookie", cookies)
//                .execute();
        browser.get(articleUrl1);

//        log.info("文章内容：{}，\n是否有登录后的卓创资讯文章内容：{}", response.body(), response.body().contains("热轧夜盘再次下挫"));
//        log.info("文章内容：{}，\n是否有登录后的兰格资讯文章内容：{}", response1.body(), response1.body().contains("↓880"));
//        log.info("文章内容：{}，\n是否有登录后的简数文章内容：{}", response.body(), response.body().contains("任务分组"));

//        log.info("文章内容：{}，\n是否有登录后的卓创资讯文章内容：{}", browser.getPageSource(), browser.getPageSource().contains("热轧夜盘再次下挫"));
        log.info("文章内容：{}，\n是否有登录后的兰格资讯文章内容：{}", browser.getPageSource(), browser.getPageSource().contains("↓880"));
//        log.info("文章内容：{}，\n是否有登录后的兰格资讯文章内容：{}", browser.getPageSource(), browser.getPageSource().contains("过磅含税价"));
//        log.info("文章内容：{}，\n是否有登录后的简数文章内容：{}", browser.getPageSource(), browser.getPageSource().contains("任务分组"));
//        log.info("文章内容：{}，\n是否有登录后的简数文章内容：{}", browser.getPageSource(), browser.getPageSource().contains("Bad、boy"));
        browser.quit();

//        getBrowser();
//        // 先访问，不然添加cookie会报错:invalid cookie domain
//        browser.get(loginUrl);
//        browser.manage().deleteAllCookies();
//
//        // 添加cookie
////        for (Cookie cookie : cookieList) {
////            browser.manage().addCookie(cookie);
////        }
//        for (String cookie : cookieList) {
//            String cookieName = cookie.substring(0, cookie.indexOf("="));
//            String cookieValue = cookie.substring(cookie.indexOf("=") + 1);
//            browser.manage().addCookie(new Cookie(cookieName, cookieValue));
//        }
//        browser.get(articleUrl2);
//        // 运行后关闭浏览器
//        browser.quit();
//
////        HttpResponse response2 = HttpRequest.get(articleUrl2)
////                .header("cookie", cookies)
////                .execute();
//        log.info("文章内容：{}，\n是否有登录后的兰格资讯文章内容：{}", browser.getPageSource(), browser.getPageSource().contains("过磅含税价"));
//        log.info("文章内容：{}，\n是否有登录后的兰格资讯文章内容：{}", response2.body(), response2.body().contains("过磅含税价"));
    }

    @Test
    public void addCookieLogin() {
        HttpResponse response1 = HttpRequest.get(articleUrl1)
                .header("cookie", cookies)
                .execute();
        log.info("文章内容：{}，\n是否有登录后的兰格资讯文章内容：{}", response1.body(), response1.body().contains("↓880"));
    }


    /**
     * 获取微信公众号登录cookies
     *
     * @throws Exception
     */
    @Test
    public void getWeChatCookies() throws Exception {
        // 浏览器驱动配置
        getBrowser();
        // 测试微信公众号登录网站
        String loginUrl = "https://mp.weixin.qq.com/";
        browser.get(loginUrl);

        // 持续等待，直到扫码完成（扫码登录上去cookies的size=13）
        long waitTime = 1L;
        int waitCount = 1;
        while (browser.manage().getCookies().size() < 13) {
            TimeUnit.SECONDS.sleep(waitTime);
            log.info("等待时间：{} 秒", waitTime * waitCount);
            waitCount++;
        }

        // 获取cookies
        StringBuilder cookies = new StringBuilder();
        browser.manage().getCookies().forEach(cookie -> {
            cookies.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        });
        log.info("获取到的cookies：{}", cookies);
    }

    /**
     * 获取Selenium浏览器驱动
     */
    private void getBrowser() {
        System.setProperty("webdriver.chrome.driver", driverPath);
        browser = new ChromeDriver();
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
}