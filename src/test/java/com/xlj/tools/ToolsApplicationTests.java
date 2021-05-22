package com.xlj.tools;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xlj.tools.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
//@SpringBootTest
public class ToolsApplicationTests {
    @Autowired
    RedissonClient redissonClient;

    @Test
    void contextLoads() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        int a = 10 - 9;
        int b = 1 / a;
        int c = 1 / (10 - 9);
    }

    @Test
    public void redissonTest() {
        log.info("redisson整合：{}", redissonClient.toString());
    }
}
