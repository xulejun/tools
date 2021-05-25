package com.xlj.tools;

import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;


/**
 * selenium整合测试
 *
 * @author xlj
 * @date 2021/5/25
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ToolsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeleniumTest {

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
    public void addBookToEmptyList() throws InterruptedException {
        // 浏览器驱动配置
        System.setProperty("webdriver.chrome.driver", driverPath);
        browser = new ChromeDriver();
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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
}