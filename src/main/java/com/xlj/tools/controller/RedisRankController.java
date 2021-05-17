package com.xlj.tools.controller;

import com.xlj.tools.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * redis 排行榜
 *
 * @author xlj
 * @date 2021/5/17
 */
@Slf4j
@RestController
public class RedisRankController {
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/getHour")
    public Set getHour() {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        Set<ZSetOperations.TypedTuple<Integer>> range = redisTemplate.opsForZSet().reverseRangeWithScores(RedisConstant.HOUR_KEY + hour, 0, 30);
        return range;
    }
    @GetMapping("/getDay")
    public Set getDay() {
        Set<ZSetOperations.TypedTuple<Integer>> range = redisTemplate.opsForZSet().reverseRangeWithScores(RedisConstant.DAY_KEY, 0, 30);
        return range;
    }
    @GetMapping("/getWeek")
    public Set getWeek() {
        Set<ZSetOperations.TypedTuple<Integer>> range = redisTemplate.opsForZSet().reverseRangeWithScores(RedisConstant.WEEK_KEY, 0, 30);
        return range;
    }
    @GetMapping("/getMonth")
    public Set getMonth() {
        Set<ZSetOperations.TypedTuple<Integer>> range = redisTemplate.opsForZSet().reverseRangeWithScores(RedisConstant.MONTH_KEY, 0, 30);
        return range;
    }
}
