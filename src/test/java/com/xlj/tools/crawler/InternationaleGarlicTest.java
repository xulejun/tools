package com.xlj.tools.crawler;

import cn.hutool.http.HttpRequest;
import com.xlj.tools.schedule.GarlicJob;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 国级大蒜贸易
 *
 * @author legend xu
 * @date 2021/12/16
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class InternationaleGarlicTest {
    @Autowired
    GarlicJob garlicJob;

    @Test
    public void methodTest() throws Exception {
        String url = "http://www.51garlic.com/jg/show-htm-itemid-104251.html";
        // 数据入库
        garlicJob.detailUrl(url);
    }

    public static void main(String[] args) {
        // 列表采集
        String listUrl = "http://www.51garlic.com/jg/list-57.html";
        list(listUrl);
        // 详情采集
        String detailUrl = "http://www.51garlic.com/jg/show-htm-itemid-104310.html";
        detail(detailUrl);
    }

    public static void list(String listUrl) {
        String body = HttpRequest.get(listUrl).timeout(15000).execute().body();
        Elements elements = Jsoup.parse(body).body().select(".td-lm-list a");
        for (Element element : elements) {
            // 文章标题
            String title = element.attr("title");
            if (title.contains("杞县大蒜价格")) {
                // 详情链接地址
                String href = element.attr("href");
                // 文章发布时间
                String articleTime = element.parent().select("span").html();
                // 详情请求
                detail(href);
            }
        }
    }

    public static void detail(String detailUrl) {
        String detailBody = HttpRequest.get(detailUrl).timeout(15000).execute().body();
        String article = Jsoup.parse(detailBody).body().select("#article").html();
        int index = article.indexOf("印尼货：");
        // 价格
        String price = article.substring(index + 4, index + 11);
    }
}
