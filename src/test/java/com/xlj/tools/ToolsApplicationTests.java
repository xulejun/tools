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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xlj.tools.bean.User;
import com.xlj.tools.enums.InfoTypeEnum;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.redisson.api.RedissonClient;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
//@SpringBootTest
public class ToolsApplicationTests {
    @Autowired
    RedissonClient redissonClient;

    private static int a = 1;
    private static int b = 2;


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
//        System.out.println(myClass == myClass1);
//        String name = this.getClass().getSimpleName();
//        System.out.println(name);
        myClass.className();
    }


    class MyClass {
        int value;

        public MyClass(int value) {
        }

        public void className() {
            System.out.println(this.getClass().getSimpleName());
        }
    }

    public static void hello(String a) {
        // hello
        System.out.println("hello：" + a);
    }

    public static void hello(String a, String b) {
        System.out.println("hello：" + a + "hello:" + b);
    }


    public static void main(String[] args) throws Exception {
        List<User> list = Lists.newArrayList();
        a(list);
        list.forEach(System.out::println);
    }

    public static void a(List<User> list) {
        for (int i = 0; i < 3; i++) {
            User xlj = new User(i, "xlj");
            list.add(xlj);
        }
    }


    private static List<String> dateInterval(String start, String end) {
        String[] startDateArr = start.split("-");
        String[] endDateArr = end.split("-");
        // 设置开始日期
        LocalDate startDate = LocalDate.of(Integer.parseInt(startDateArr[0]), Integer.parseInt(startDateArr[1]), Integer.parseInt(startDateArr[2]));
        // 结束日期
        LocalDate endDate = LocalDate.of(Integer.parseInt(endDateArr[0]), Integer.parseInt(endDateArr[1]), Integer.parseInt(endDateArr[2]));
        // 获取时间区间
        long betweenDays = ChronoUnit.DAYS.between(startDate, endDate);
        List<LocalDate> dateList = IntStream.iterate(0, i -> i + 1).limit(betweenDays + 1)
                .mapToObj(startDate::plusDays).collect(Collectors.toList());

        return dateList.stream().map(LocalDate::toString).collect(Collectors.toList());
    }


    public static void updateString(String str) {
        if (StrUtil.isBlank(str)) {
            str = "hello";
        }
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
