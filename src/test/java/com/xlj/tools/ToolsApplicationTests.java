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
import org.junit.Test;
import org.openqa.selenium.Cookie;
import org.redisson.api.RedissonClient;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
//@SpringBootTest
public class ToolsApplicationTests {
    @Test
    public void methodTest() throws Exception{
        System.out.println("");
    }

    public static void main(String[] args) throws Exception {
    }
}
