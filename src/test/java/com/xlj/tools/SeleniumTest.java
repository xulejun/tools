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
    // 兰格cookie
    private static String cookies = "__utmc=118792214; ASP.NET_SessionId=u2zv51by5x5odhuj4j1n3s55; ASPSESSIONIDSGCSTBAT=NMOGBIPAECGFNDBALEOMAOEJ; ASPSESSIONIDSGCQQACR=EKIOPDMBDNMDCPKBJMMACDOC; ASPSESSIONIDQEARTCDQ=HPKINPICMDDJHPOBHLBLJHEL; ASPSESSIONIDQEASQBBT=NLJENLFDIBFKCBFJHGOBJJDH; Hm_lvt_4df7897ce5ff84810a1a7e9a1ace8249=1621923874,1622015315,1622165842,1622183404; __utma=118792214.842640809.1622015328.1622172192.1622183404.11; __utmz=118792214.1622183404.11.7.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmt=1; ASPSESSIONIDQEBSRBCR=KLMDKHCABANLPACOGBINFHID; __utmt_~1=1; __utmt_~2=1; __utmt_~3=1; Hm_lpvt_4df7897ce5ff84810a1a7e9a1ace8249=1622183597; __utmb=118792214.27.10.1622183404; lgmi=ClientID=badboy&userprivilege=infotest&Mytime=2021/5/28 14:33:17";
    // 中联钢
//    private static String cookies = "JSESSIONID=227D64ACE16C87F152FE252BA249631E; Hm_lvt_59843712887bd8aab5a8307c8f9215ac=1622171883,1622173717; Hm_lpvt_59843712887bd8aab5a8307c8f9215ac=1622173794";
    // 卓创cookie
//    private static String cookies = "route=5381fa73df88cce076c9e01d13c9b378; ASP.NET_SessionId=pzpcrdkmkh2rmnmaiqfdkhmx; guid=663cc3b7-81d9-aa4b-d2a7-f8df9b7d39ac; UM_distinctid=179a8021b789b-0721b5da1940a2-343c5606-1fa400-179a8021b7a3d1; href=https%3A%2F%2Fwww.sci99.com%2F; accessId=b101a8c0-85cc-11ea-b67c-831fe7f7f53e; CNZZDATA1269807659=2024453938-1622016720-%7C1622181765; BAIDU_SSP_lcr=https://www.baidu.com/link?url=25AP8OFrtqJiUGGFPzfAdsFvs_2ejM3KrpIezV8r22e&wd=&eqid=b7aac4c7000505d90000000660b08a92; qimo_seokeywords_b101a8c0-85cc-11ea-b67c-831fe7f7f53e=; isCloseOrderZHLayer=0; Hm_lvt_44c27e8e603ca3b625b6b1e9c35d712d=1622013217,1622021865,1622182559,1622182598; STATcUrl=https://www.sci99.com/undefined; Hm_lpvt_44c27e8e603ca3b625b6b1e9c35d712d=1622182745; STATReferrerIndexId=1; qimo_seosource_b101a8c0-85cc-11ea-b67c-831fe7f7f53e=%E7%AB%99%E5%86%85; pageViewNum=12";


    // 中联钢
//    String loginUrl = "http://www.custeel.com";
//    String articleUrl1 = "http://www.custeel.com/reform/view.mv?articleID=6554698&group=1013006002&cat=";   // 不锈钢废钢频道
//    String articleUrl2 = "http://www.custeel.com/reform/view.mv?articleID=6554699&group=1001&cat=1006002";   // 线螺频道
    // 卓创登录url
//        String loginUrl = "https://www.sci99.com";
//        String articleUrl1 = "https://oil.chem99.com/news/33510733.html";    // 能源频道
//        String articleUrl11 = "https://oil.chem99.com/news/33511489.html";    // 能源频道
//        String articleUrl2 = "https://chem.chem99.com/news/38444006.html";    // 化工频道
    // 兰格
    String loginUrl = "https://www.lgmi.com";
    String articleUrl1 = "https://jiancai.lgmi.com/hangqing/2021/05/28/A0101_A11.htm";  // 建材频道
    String articleUrl2 = "https://tegang.lgmi.com/hangqing/2021/05/28/A0201_C320302.htm"; // 特钢频道
    // qq邮箱
//        String loginUrl = "https://mail.qq.com";
//        String articleUrl = "https://mail.qq.com/cgi-bin/setting4?fun=list&acc=1&sid=NWkP8VOop7kUNnIS&go=mybirthinfo";

    private static ChromeDriver browser;

    // 驱动路径（在resource/driver下有该驱动）
    @Value("${selenium.webDriverPath}")
    private String driverPath;

    /**
     * Selenium 贴cookie预登陆
     */
    @Test
    public void bySeleniumLogin() {
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
        List<String> cookieList = StrUtil.splitTrim(cookies, ";");
        for (String cookie : cookieList) {
            String cookieName = cookie.substring(0, cookie.indexOf("="));
            String cookieValue = cookie.substring(cookie.indexOf("=") + 1);
            String domain = loginUrl.substring(loginUrl.indexOf("."));
            browser.manage().addCookie(new Cookie(cookieName, cookieValue, domain, "/", null));
        }

        // 文章请求
        browser.get(articleUrl2);

//        log.info("文章内容：{}，\n是否有登录后的卓创资讯文章内容：{}", browser.getPageSource(), browser.getPageSource().contains("热轧夜盘再次下挫"));

//        log.info("文章内容：{}，\n是否有登录后的兰格资讯文章内容：{}", browser.getPageSource(), browser.getPageSource().contains("↑100"));
        log.info("文章内容：{}，\n是否有登录后的兰格资讯文章内容：{}", browser.getPageSource(), browser.getPageSource().contains("↓200"));

//        log.info("文章内容：{}，\n是否有登录后的兰格资讯文章内容：{}", browser.getPageSource(), browser.getPageSource().contains("瑞丰(协议户"));

        browser.quit();
    }

    /**
     * HttpRequest 贴cookie预登陆
     */
    @Test
    public void byHttpRequestLogin() {
        HttpResponse response1 = HttpRequest.get(articleUrl2)
                .header("cookie", cookies)
                .setMaxRedirectCount(10)
                .execute();
//        log.info("文章内容：{}，\n是否有登录后的兰格资讯文章内容：{}", response1.body(), response1.body().contains("↓880"));
//        log.info("文章内容：{}，\n是否有登录后的兰格资讯文章内容：{}", response1.body(), response1.body().contains("过磅含税价"));
//        log.info("文章内容：{}，\n是否有登录后的兰格资讯文章内容：{}", response1.body(), response1.body().contains("↓50"));

//        log.info("文章内容：{}，\n是否有登录后的中联钢文章内容：{}", response1.body(), response1.body().contains("304（Ni≈8%）"));
//        log.info("文章内容：{}，\n是否有登录后的中联钢文章内容：{}", response1.body(), response1.body().contains("瑞丰(协议户"));

//        log.info("文章内容：{}，\n是否有登录后的卓创文章内容：{}", response1.body(), response1.body().contains("没有浏览该信息的权限"));

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