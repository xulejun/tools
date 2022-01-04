package com.xlj.tools.schedule;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import com.google.common.collect.Maps;
import com.xlj.tools.bean.Garlic;
import com.xlj.tools.service.GarlicService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author legend xu
 * @date 2021/12/16
 */
@Slf4j
//@Component
//@EnableScheduling
public class GarlicJob {
    @Autowired
    GarlicService garlicService;

        @Scheduled(fixedDelay = 1000)     // cron表达式：当前方法执行完毕后，再过1s后执行此方法
//    @Scheduled(cron = "0 0 12 * * ? ")  // 每天中午12点执行
    public void startJob() throws Exception {
//        DateTime dateTime = new DateTime();
//        String dateStr = dateTime.toString();
//        // 取出年月日 yyyy-MM-dd
//        String date = dateStr.substring(0, dateStr.indexOf(" ") + 1);
//        String startTimeStr = date.concat("08:00:00");
//        String endTimeStr = date.concat("23:00:00");
//        Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTimeStr);
//        Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTimeStr);
        // 处于策略范围内，则采集
//        if (dateTime.isIn(startTime, endTime)) {
        try {
            list();
        } catch (Exception e) {
            log.warn("garlic采集异常：", e);
        }
//        }
    }

    /**
     * 列表采集
     */
    public void list() throws InterruptedException {
        Garlic garlic;
        for (int i = 2; i < 3; i++) {
            try {
                String url = "http://www.51garlic.com/jg/list-57-" + i + ".html";
                log.info("garlic当前采集列表：url={}", url);
                String body = HttpRequest.get(url).timeout(15000).execute().body();
                Elements elements = Jsoup.parse(body).body().select(".td-lm-list a");
                for (Element element : elements) {
                    // 文章标题
                    String title = element.attr("title");
                    if (title.contains("杞县大蒜价格")) {
                        // 详情链接地址
                        String href = element.attr("href");
                        // 文章发布时间
                        String articleTime = element.parent().select("span").html();
                        garlic = Garlic.builder().title(title).detailUrl(href).articleTime(articleTime).crawlerTime(new Date()).build();
                        // 详情请求
                        detail(garlic);
                    }
                }
            } catch (Exception e) {
                log.warn("garlic列表采集异常：", e);
            } finally {
                TimeUnit.MINUTES.sleep(RandomUtil.randomLong(1, 5));
            }
        }
    }

    /**
     * 详情采集
     *
     * @param garlic
     */
    public void detail(Garlic garlic) throws InterruptedException {
        try {
            String detailBody = HttpRequest.get(garlic.getDetailUrl()).timeout(15000).execute().body();
            String article = Jsoup.parse(detailBody).body().select("#article").html();
            int index = article.indexOf("印尼货：");
            BigDecimal price = index == -1 ? BigDecimal.valueOf(0) :
                    BigDecimal.valueOf(Double.parseDouble(article.substring(index + 4, index + 8)));
            // 价格
            garlic.setPrice(price);
            garlicService.replace(garlic);
        } catch (Exception e) {
            log.warn("garlic详情采集异常：url={}", garlic.getDetailUrl(), e);
        } finally {
            TimeUnit.MINUTES.sleep(RandomUtil.randomLong(1, 5));
        }
    }

    public void detailUrl(String url) {
        try {
            String detailBody = HttpRequest.get(url).timeout(15000).execute().body();
            String article = Jsoup.parse(detailBody).body().select("#article").html();
            int index = article.indexOf("印尼货：");
            // 价格
            String price = article.substring(index + 4, index + 8);
            Garlic garlic = new Garlic();
            garlic.setPrice(BigDecimal.valueOf(Double.parseDouble(price)));
            garlicService.insert(garlic);
        } catch (Exception e) {
            log.warn("garlic详情采集异常:", e);
        }
    }

}
