package com.xlj.tools;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xlj.tools.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
//@SpringBootTest
public class ToolsApplicationTests {
    @Autowired
    RedissonClient redissonClient;

    @Test
    void contextLoads() throws IOException {
        String cookies = "__utmc=118792214; ASP.NET_SessionId=u2zv51by5x5odhuj4j1n3s55; ASPSESSIONIDSGCSTBAT=NMOGBIPAECGFNDBALEOMAOEJ; ASPSESSIONIDSGCQQACR=EKIOPDMBDNMDCPKBJMMACDOC; ASPSESSIONIDQEARTCDQ=HPKINPICMDDJHPOBHLBLJHEL; Hm_lvt_4df7897ce5ff84810a1a7e9a1ace8249=1621818337,1621923874,1622015315; __utma=118792214.842640809.1622015328.1622020207.1622081999.3; __utmz=118792214.1622081999.3.3.utmcsr=login.lgmi.com|utmccn=(referral)|utmcmd=referral|utmcct=/; __utmt=1; ASPSESSIONIDQEASQBBT=BPMLLLFDCDGJGGBHCGHNAAGD; Hm_lpvt_4df7897ce5ff84810a1a7e9a1ace8249=1622082452; __utmb=118792214.6.10.1622081999; lgmi=Mytime=2021%2F5%2F2710%3A27%3A22&jcpd=jiancai&userprivilege=infotest&ClientID=badboy";
        List<String> cookieList = StrUtil.splitTrim(cookies, ";");
        for (String cookie : cookieList) {
            String cookieName = cookie.substring(0, cookie.indexOf("="));
            String cookieValue = cookie.substring(cookie.indexOf("=") + 1);
            System.out.println(cookieName + ":" + cookieValue);
        }
    }

    @Test
    public void methodTest() {
        String articleUrl = "http://www.foretech.com.cn/list-66.html";
        HttpResponse response = HttpRequest.get(articleUrl)
                .execute();
        log.info("静态解析——文章内容：\n{}", response.body());
    }

    public static void main(String[] args) {

        String str1 = new StringBuilder("58").append("tongcheng").toString();
        System.out.println(str1);
        System.out.println(str1.intern());
        System.out.println(str1 == str1.intern());

        System.out.println("------------");

        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2);
        System.out.println(str2.intern());
        System.out.println(str2 == str2.intern());

    }

    static Object objectLockA = new Object();

    public static void m1() {
        new Thread(() -> {
            synchronized (objectLockA) {
                System.out.println(Thread.currentThread().getName() + "\t" + "------外层调用");
                synchronized (objectLockA) {
                    System.out.println(Thread.currentThread().getName() + "\t" + "------中层调用");
                    synchronized (objectLockA) {
                        System.out.println(Thread.currentThread().getName() + "\t" + "------内层调用");
                    }
                }
            }
        }, "t1").start();

    }

}
