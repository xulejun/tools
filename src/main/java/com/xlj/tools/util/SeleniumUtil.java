package com.xlj.tools.util;

import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

/**
 * @author xlj
 * @date 2021/6/15
 */
public class SeleniumUtil {
    public static ChromeDriver browser;

    /**
     * 驱动路径（在resource/driver下有该驱动）
     */
    private static final String DRIVER_PATH = "D:/webdriver/chromedriver-89.exe";

    /**
     * 获取Selenium浏览器驱动
     */
    public static void getBrowser() {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        browser = new ChromeDriver();
        browser.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }
}
