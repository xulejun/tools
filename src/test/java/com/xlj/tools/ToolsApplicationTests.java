package com.xlj.tools;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xlj.tools.bean.User;
import com.xlj.tools.enums.InfoTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.*;
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
        MyClass myClass = new MyClass(2);
        MyClass myClass1 = new MyClass(2);
        System.out.println(myClass == myClass1);
    }

    class MyClass {
        int value;

        public MyClass(int value) {
        }
    }

    public static void main(String[] args) {
    }

    public int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    result[0] = i;
                    result[1] = j;
                    break;
                }
            }
        }
        return result;
    }

}
