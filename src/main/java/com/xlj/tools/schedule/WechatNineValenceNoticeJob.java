package com.xlj.tools.schedule;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Maps;
import com.xlj.tools.wechat.ninevalence.AccountNineValenceNotice;
import com.xlj.tools.wechat.ninevalence.AppletMaternityCareNotice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 微信公众号九价文章采集通知
 *
 * @author legend xu
 * @date 2021/11/29
 */
@Slf4j
@Component
@EnableScheduling
public class WechatNineValenceNoticeJob {
    @Value("${wechat.cookie}")
    private String cookie;

    @Autowired
    private AccountNineValenceNotice nineValenceNotice;

    @Autowired
    private AppletMaternityCareNotice appletMaternityCareNotice;

    /**
     * 采集文章的时间范围
     */
    private static Map<String, String> timeRange = Maps.newHashMap();

    /**
     * 不采集小程序的时间范围
     */
    private static Map<String, String> appletTimeRange = Maps.newHashMap();

    static {
        timeRange.put("07:55:00", "08:01:00");
        timeRange.put("10:55:00", "11:01:00");
        timeRange.put("11:59:00", "12:20:00");
        timeRange.put("12:25:00", "12:31:00");
        timeRange.put("16:40:00", "17:01:00");
        timeRange.put("18:55:00", "19:01:00");
        timeRange.put("19:55:00", "20:01:00");
        timeRange.put("21:55:00", "22:01:00");
        appletTimeRange.put("23:00:00", "24:00:00");
        appletTimeRange.put("00:00:00", "08:00:00");
    }

    @Scheduled(fixedDelay = 1000)
    public void nineValenceTest() throws Exception {
        DateTime dateTime = new DateTime();
        String dateStr = dateTime.toString();
        // 取出年月日 yyyy-MM-dd
        String date = dateStr.substring(0, dateStr.indexOf(" ") + 1);
        for (String start : timeRange.keySet()) {
            String end = timeRange.get(start);
            String startTimeStr = date.concat(start);
            String endTimeStr = date.concat(end);
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTimeStr);
            Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTimeStr);
//            log.info("1开始时间={}，结束时间={}", startTimeStr, endTimeStr);
            // 处于策略范围内，则采集
            if (dateTime.isIn(startTime, endTime)) {
                try {
                    nineValenceNotice.notice();
                } catch (Exception e) {
                    log.warn("采集文章失败：", e);
                    return;
                }
                break;
            }
            TimeUnit.SECONDS.sleep(RandomUtil.randomLong(1, 3));
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void maternityCareAppletNotice() throws Exception {
        DateTime dateTime = new DateTime();
        String dateStr = dateTime.toString();
        // 取出年月日 yyyy-MM-dd
        String date = dateStr.substring(0, dateStr.indexOf(" ") + 1);
        for (String start : appletTimeRange.keySet()) {
            String end = appletTimeRange.get(start);
            String startTimeStr = date.concat(start);
            String endTimeStr = date.concat(end);
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTimeStr);
            Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTimeStr);
//            log.info("2开始时间={}，结束时间={}", startTimeStr, endTimeStr);
            // 处于策略范围内，则采集
            if (!dateTime.isIn(startTime, endTime)) {
                try {
                    appletMaternityCareNotice.notice();
                } catch (Exception e) {
                    log.warn("采集文章失败：", e);
                    return;
                }
                break;
            }
        }
        TimeUnit.MINUTES.sleep(RandomUtil.randomLong(20, 40));
    }
}