package com.xlj.tools;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xlj.tools.util.SeleniumUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;


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

    private static ChromeDriver browser;

    /**
     * 自动配置模拟登录解析
     */
    @Test
    public void autoClickGetCookie() throws Exception {
        String loginUrl = "https://oil.chem99.com/include/loginframetop.aspx";
        String articleUrl = "https://oil.chem99.com/news/38443008.html";

        String loginViewXpath = "";
        String loginPreXpath = "";
        String userName = "bad_boy";
        String password = "xulejun520.";
        String userNameXpath = "//*[@id=\"chemname\"]";
        String passwordXpath = "//*[@id=\"chempwd\"]";
        String loginXpath = "//*[@id=\"Btn_Login\"]";
        // 模拟登录获取cookies
        String cookies = mockLogin(loginUrl, userName, password, userNameXpath, passwordXpath, loginXpath, loginViewXpath, loginPreXpath);

        // 静态采集
        staticParse(cookies, articleUrl, null, null);

        // 动态采集
        dynamicParse(cookies, articleUrl, loginUrl);
    }

    private String mockLogin(String loginUrl, String userName, String password,
                             String userNameXpath, String passwordXpath, String loginXpath, String loginViewXpath, String loginPreXpath) throws Exception {
        SeleniumUtil.getBrowser();
        browser.get(loginUrl);
        if (StrUtil.isNotBlank(loginViewXpath)) {
            browser.findElementByXPath(loginViewXpath).click();
            // 等待页面登录响应，todo 完善等待时间
            TimeUnit.SECONDS.sleep(1);
        }
        if (StrUtil.isNotBlank(loginPreXpath)) {
            browser.findElementByXPath(loginPreXpath).click();
            // 等待页面登录响应，todo 完善等待时间
            TimeUnit.SECONDS.sleep(1);
        }
        browser.findElementByXPath(userNameXpath).sendKeys(userName);
        browser.findElementByXPath(passwordXpath).sendKeys(password);
        browser.findElementByXPath(loginXpath).click();
        // 等待页面登录响应，todo 待完善
        TimeUnit.SECONDS.sleep(10);
        // 获取cookies
        StringBuilder cookies = new StringBuilder();
        browser.manage().getCookies().forEach(cookie -> {
            cookies.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        });
        log.info("获取到的cookies：{}", cookies);
        browser.quit();
        return cookies.toString();
    }

    /**
     * 手动贴cookie采集解析
     */
    @Test
    public void manualGetCookie() throws Exception {
        String loginUrl = "https://mail.qq.com/";
        String articleUrl = "https://mail.qq.com/cgi-bin/readmail?folderid=3&folderkey=&t=readmail&mailid=ZC0007_ClbN03GMLDIuK28ADVygbb5&mode=pre&maxage=3600&base=12.5&ver=10705&sid=TRFt5EYyfWPaMnYW";

        // selenium获取cookies
        String cookies = getCookie(loginUrl);

        // 静态采集
        staticParse(cookies, articleUrl, null, null);

        // 动态采集
        dynamicParse(cookies, articleUrl, loginUrl);
    }

    /**
     * Selenium动态采集
     *
     * @param cookies
     */
    private void dynamicParse(String cookies, String articleUrl, String loginUrl) {
        SeleniumUtil.getBrowser();
        // 先访问页面的登录页，目的是给cookie加domain，不然会报错:invalid cookie domain
        browser.get(loginUrl);
        // 因为上面只是访问登录页，并未做真正的登录，这里删除访问后的cookie
        browser.manage().deleteAllCookies();
        // 添加登录后的cookie
        List<String> cookieList = StrUtil.splitTrim(cookies, ";");
        for (String cookie : cookieList) {
            String cookieName = cookie.substring(0, cookie.indexOf("="));
            String cookieValue = cookie.substring(cookie.indexOf("=") + 1);
            // todo loginUrl要做校验，这里要给个策略拿出domain
            String tempDomain = articleUrl.substring(articleUrl.indexOf("."));
            String domain = tempDomain.substring(0, tempDomain.indexOf("/"));
            browser.manage().addCookie(new Cookie(cookieName, cookieValue, domain, "/", null));
        }

        // 文章请求 todo js脚本渲染等待时间
        browser.get(articleUrl);
        //        new WebDriverWait(browser, 100, 1000)
//                .until(ExpectedConditions.visibilityOf(browser.findElement(By.cssSelector(".viewer-main-title>.item-teaser>.item-teaser-header>.item-teaser-title"))));
        log.info("动态解析——文章内容：\n{}", browser.getPageSource());
        browser.quit();
    }

    /**
     * HttpRequest静态采集
     *
     * @param cookies
     */
    private void staticParse(String cookies, String articleUrl, String host, String referer) {
        HttpResponse response = HttpRequest.get(articleUrl)
                .header("cookie", cookies)
                .header("Host", host)
                .header("Referer", referer)
                .execute();
        log.info("静态解析——文章内容：\n{}", response.body());
    }

    /**
     * 获取cookies
     *
     * @return
     * @throws Exception
     */
    private String getCookie(String loginUrl) throws Exception {
        SeleniumUtil.getBrowser();
        // todo 完善等待时间
        browser.get(loginUrl);
        TimeUnit.SECONDS.sleep(20);

        // 获取cookies
        StringBuilder cookies = new StringBuilder();
        browser.manage().getCookies().forEach(cookie -> {
            cookies.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        });
        log.info("获取到的cookies：{}", cookies.toString());
        // 退出浏览器
        browser.quit();
        return cookies.toString();
    }

    /**
     * 获取微信公众号登录cookies
     *
     * @throws Exception
     */
    @Test
    public void getWeChatCookies() throws Exception {
        // 浏览器驱动配置
        SeleniumUtil.getBrowser();
        // 测试微信公众号登录网站
        String loginUrl = "https://mp.weixin.qq.com/";
        browser.get(loginUrl);

        // 持续等待，直到扫码完成（扫码登录上去cookies的size=13）
        long waitTime = 1L;
        int waitCount = 1;
        while (browser.manage().getCookies().size() < 12) {
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
        browser.close();
    }
}