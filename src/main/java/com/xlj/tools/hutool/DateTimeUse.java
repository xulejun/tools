package com.xlj.tools.hutool;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * hutool时间工具的使用
 *
 * @author xlj
 * @date 2021/5/8 10:05
 */
@Slf4j
public class DateTimeUse {
    public static void main(String[] args) {
        // 性能测试
//        performance();
        // 字符串转日期
        String date1 = "2021-05-08";
        DateTime dateTime = DateUtil.parse(date1);
        log.info("字符串转日期：{}", dateTime);
        // 日期转字符串
        Date date = new Date();
        String format = DateUtil.format(date, "yyyy/MM/dd");
        log.info("日期转字符串：{}", format);
        // 根据出生年月计算年龄
        String birthday = "1998-05-26";
        int age = DateUtil.ageOfNow(birthday);
        log.info("你的出生年月为：{}，当前 {} 岁", birthday, age);
    }

    /**
     * 性能测试
     */
    private static void performance() {
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

    private static void forloop(int total) {
        for (int i = 0; i < total; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
