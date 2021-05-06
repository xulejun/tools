package com.xlj.tools;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xlj.tools.bean.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@SpringBootTest
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

}
