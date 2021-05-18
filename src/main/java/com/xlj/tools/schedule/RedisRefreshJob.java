package com.xlj.tools.schedule;

import cn.hutool.core.util.RandomUtil;
import com.xlj.tools.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis 定时合并热点数据
 *
 * @author xlj
 * @date 2021/5/17
 */
@Slf4j
//@Component
//@EnableScheduling
public class RedisRefreshJob {
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        log.info("redis定时合并热点数据开始了");
    }

    /**
     * 每 5s 为热点标题随机增加score积分
     */
    @Scheduled(fixedDelay = 5000)
    public void refreshHour() {
        // 计算当前时间的key
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        // 为26个英文字母（标题）增加score积分
        for (int i = 1; i <= 26; i++) {
            redisTemplate.opsForZSet().incrementScore(RedisConstant.HOUR_KEY + hour, String.valueOf((char) (96 + i)), RandomUtil.randomInt(10));
        }
    }

    /**
     * 定时1hour，合并统计天、周、月的排行榜
     */
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void refreshKey() {
        // 计算当前时间的key
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        // 刷新天数据
        refreshDay(hour);
        // 刷新周数据
        refreshWeek(hour);
        // 刷新月数据
        refreshMonth(hour);

    }

    private void refreshMonth(long hour) {
        List<String> keyList = new ArrayList<>();
        for (int i = 1; i < 24 * 30; i++) {
            String key = RedisConstant.HOUR_KEY + (hour - i);
            keyList.add(key);
        }
        // 把当前时间key，并且把后推24*7-1小时，共计24*7小时，求出并集放入week_key
        redisTemplate.opsForZSet().unionAndStore(RedisConstant.HOUR_KEY + hour, keyList, RedisConstant.MONTH_KEY);
        log.info("按月排行热点key刷新完成");
    }

    private void refreshWeek(long hour) {
        List<String> keyList = new ArrayList<>();
        for (int i = 1; i < 24 * 7; i++) {
            String key = RedisConstant.HOUR_KEY + (hour - i);
            keyList.add(key);
        }
        // 把当前时间key，并且把后推24*7-1小时，共计24*7小时，求出并集放入week_key
        redisTemplate.opsForZSet().unionAndStore(RedisConstant.HOUR_KEY + hour, keyList, RedisConstant.WEEK_KEY);
        log.info("按周排行热点key刷新完成");
    }

    private void refreshDay(long hour) {
        List<String> keyList = new ArrayList<>(24);
        // 添加 24hour 的热点key
        for (int i = 1; i < 24; i++) {
            String key = RedisConstant.HOUR_KEY + (hour - i);
            keyList.add(key);
        }
        // 把当前时间key，并且把后推23小时，共计24小时，求出并集放入day_key
        redisTemplate.opsForZSet().unionAndStore(RedisConstant.HOUR_KEY + hour, keyList, RedisConstant.DAY_KEY);

        // 设置当天的key 40天过期，不然历史数据浪费内存
        for (int i = 0; i < 24; i++) {
            String key = RedisConstant.HOUR_KEY + (hour - i);
            redisTemplate.expire(key, 40, TimeUnit.DAYS);
        }
        log.info("按天排行热点key刷新完成");
    }

}
