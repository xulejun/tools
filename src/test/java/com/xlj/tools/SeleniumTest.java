package com.xlj.tools;

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
    static String jsonStr = "[{\"isSecure\":false,\"path\":\"/\",\"domain\":\".steel.sci99.com\",\"name\":\"Hm_lvt_a831ffced6eb4eda5078ef1076222e03\",\"isHttpOnly\":false,\"expiry\":1653554310000,\"value\":\"1622017952\"},{\"isSecure\":false,\"path\":\"/\",\"domain\":\"steel.sci99.com\",\"name\":\"CNZZDATA1262024308\",\"isHttpOnly\":false,\"expiry\":1637743109000,\"value\":\"1805671474-1622016471-https%253A%252F%252Fsteel.sci99.com%252F%7C1622016471\"},{\"isSecure\":false,\"path\":\"/\",\"domain\":\"steel.sci99.com\",\"name\":\"ASP.NET_SessionId\",\"isHttpOnly\":false,\"value\":\"rtanfzvb1npv4jywtlqv3ne4\"},{\"isSecure\":false,\"path\":\"/\",\"domain\":\"steel.sci99.com\",\"name\":\"route\",\"isHttpOnly\":false,\"value\":\"5381fa73df88cce076c9e01d13c9b378\"},{\"isSecure\":false,\"path\":\"/\",\"domain\":\".sci99.com\",\"name\":\"STATReferrerIndexId\",\"isHttpOnly\":false,\"expiry\":1622061510000,\"value\":\"2\"},{\"isSecure\":false,\"path\":\"/\",\"domain\":\".sci99.com\",\"name\":\"guid\",\"isHttpOnly\":false,\"expiry\":4775618269000,\"value\":\"dca5f813-abb3-6c39-21a8-12ff13e0c38a\"},{\"isSecure\":false,\"path\":\"/\",\"domain\":\".sci99.com\",\"name\":\"isCloseOrderZHLayer\",\"isHttpOnly\":false,\"expiry\":1622061469000,\"value\":\"0\"},{\"isSecure\":false,\"path\":\"/\",\"domain\":\".steel.sci99.com\",\"name\":\"Hm_lpvt_a831ffced6eb4eda5078ef1076222e03\",\"isHttpOnly\":false,\"value\":\"1622018310\"},{\"isSecure\":false,\"path\":\"/\",\"domain\":\".sci99.com\",\"name\":\"UM_distinctid\",\"isHttpOnly\":false,\"expiry\":1637743069000,\"value\":\"179a7d2a10d24d-02669abec06cc5-343c5606-1fa400-179a7d2a10e60f\"}]";
    private static ChromeDriver browser;

    // 驱动路径（在resource/driver下有该驱动）
    @Value("${selenium.webDriverPath}")
    private String driverPath;

    /**
     * 运行后关闭浏览器
     */
    @AfterClass
    public static void closeBrowser() {
        browser.quit();
    }

    @Test
    public void preLoginTest() throws Exception {
        getBrowser();

        // 卓创登录url
//        String loginUrl = "https://www.sci99.com/";
        String loginUrl = "https://www.sci99.com/include/scilogin.aspx";
        String articleUrl = "https://steel.sci99.com/news/38363817.html";
        // 兰格
//        String loginUrl = "https://info.lgmi.com/login_news/login.aspx";
//        String loginUrl = "https://www.lgmi.com/";
//        String articleUrl = "https://jiancai.lgmi.com/html.aspx?cityid=&articleid=A1102&recordno=231283&u=/huizong/2021/05/24/A1102_a0f951.htm";
        // 简数
//        String loginUrl = "http://account.keydatas.com/login";
//        String articleUrl = "http://dash.keydatas.com/task/group/list";
        // qq邮箱
//        String loginUrl = "https://mail.qq.com";
//        String articleUrl = "https://mail.qq.com/cgi-bin/setting4?fun=list&acc=1&sid=NWkP8VOop7kUNnIS&go=mybirthinfo";
        browser.get(loginUrl);

        // 持续等待，直到扫码完成（扫码登录上去cookies的size=13）
//        long waitTime = 1L;
//        int waitCount = 1;
//        while (browser.manage().getCookies().size() < 2) {
//            TimeUnit.SECONDS.sleep(waitTime);
//            log.info("等待时间：{} 秒", waitTime * waitCount);
//            waitCount++;
//        }
        TimeUnit.SECONDS.sleep(20L);

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
        List<Cookie> cookieList = new ArrayList<>(browser.manage().getCookies());
        log.info("获取到的cookies：{}", cookieList);
        browser.quit();

        getBrowser();
        // 先访问，不然添加cookie会报错:invalid cookie domain
        browser.get(loginUrl);

        // 添加cookie
        for (Cookie cookie : cookieList) {
            browser.manage().addCookie(cookie);
        }

        // 文章请求
//        HttpResponse response = HttpRequest.get(articleUrl)
//                .header("cookie",cookies.toString())
//                .execute();
        browser.get(articleUrl);

//        log.info("文章内容：{}，\n是否有登录后的卓创资讯文章内容：{}", response.body(), response.body().contains("热轧夜盘再次下挫"));
//        log.info("文章内容：{}，\n是否有登录后的兰格资讯文章内容：{}", response.body(), response.body().contains("↓880"));
//        log.info("文章内容：{}，\n是否有登录后的简数文章内容：{}", response.body(), response.body().contains("任务分组"));

        log.info("文章内容：{}，\n是否有登录后的卓创资讯文章内容：{}", browser.getPageSource(), browser.getPageSource().contains("热轧夜盘再次下挫"));
//        log.info("文章内容：{}，\n是否有登录后的兰格资讯文章内容：{}", browser.getPageSource(), browser.getPageSource().contains("↓880"));
//        log.info("文章内容：{}，\n是否有登录后的简数文章内容：{}", browser.getPageSource(), browser.getPageSource().contains("任务分组"));
//        log.info("文章内容：{}，\n是否有登录后的简数文章内容：{}", browser.getPageSource(), browser.getPageSource().contains("Bad、boy"));
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

    private void getBrowser() {
        System.setProperty("webdriver.chrome.driver", driverPath);
        browser = new ChromeDriver();
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
}