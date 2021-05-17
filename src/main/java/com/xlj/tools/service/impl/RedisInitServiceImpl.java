package com.xlj.tools.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.xlj.tools.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * redis 数据初始化
 *
 * @author xlj
 * @date 2021/5/17
 */
@Slf4j
@Service
public class RedisInitServiceImpl {
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init30day() {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        // 初始化一个月的key，一天24个key
        for (int i = 1; i <= 24 * 30; i++) {
            String key = RedisConstant.HOUR_KEY + (hour - 1);
            // 一个小时内的热点
            for (int j = 1; j <= 26; j++) {
                // 采用26个字母（相当于微博热搜标题）进行排行，为每个字母生成一个随机数，作为它的score
                redisTemplate.opsForZSet().add(key, String.valueOf((char) (96 + i)), RandomUtil.randomInt(10));
            }
        }
    }
}