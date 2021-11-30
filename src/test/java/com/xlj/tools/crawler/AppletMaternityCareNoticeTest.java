package com.xlj.tools.crawler;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xlj.tools.ToolsApplication;
import com.xlj.tools.wechat.ninevalence.AppletMaternityCareNotice;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.xlj.tools.wechat.ninevalence.AppletMaternityCareNotice.*;


/**
 * @author legend xu
 * @date 2021/11/29
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ToolsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppletMaternityCareNoticeTest {
    @Autowired
    private AppletMaternityCareNotice appletMaternityCareNotice;

    @Test
    public void testRequest() {
        // 发送请求
        appletMaternityCareNotice.notice();
    }
}
