package com.xlj.tools;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xlj.tools.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

//@SpringBootTest
@Slf4j
public class ToolsApplicationTests {

    @Test
    void contextLoads() throws IOException {
        List<User> list = new ArrayList<>(5);
        for (int i = 1; i < 6; i++) {
            User user = User.builder().id(i).name("XLJ-" + i).build();
            list.add(user);
        }
        JSONArray objects = new JSONArray(list);
        List<User> users = JSONUtil.toList(objects, User.class);
    }

    /**
     * HuTool性能统计
     */
    @Test
    public void test8() {
        int loopcount = 2;
        TimeInterval timer = DateUtil.timer();
        forloop(loopcount);
        long interval1 = timer.interval();
        log.info("性能统计，总共花费了{} (毫秒数)", interval1);

        forloop(loopcount);
        long interval2 = timer.intervalRestart();
        log.info("性能统计，总共花费了 {}(毫秒数),并重置", interval2);

        forloop(loopcount);
        long interval3 = timer.interval();
        log.info("性能统计，总共花费了 {}(毫秒数)", interval3);
    }

    private void forloop(int total) {
        for (int i = 0; i < total; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
