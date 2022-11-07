package com.xlj.tools;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.RedissonLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author xlj
 * @date 2021/7/1
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ToolsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testLock() throws Exception{
        RLock lock = redissonClient.getLock("my_lock");
        lock.lock();
        TimeUnit.SECONDS.sleep(40);
    }

    @Test
    public void test() {
        String a = null;
        Optional<String> hello = Optional.ofNullable(a);
        System.out.println(hello);
        if (hello.isPresent()) {
            System.out.println("存在");
        } else {
            System.out.println("不存在");
        }
    }

    @Test
    public void testZSet() throws Exception {
        String key = "ocr:user:config";
        String value = "config";
        Jedis jedis = new Jedis("127.0.0.1", 6379);
//        for (int i = 0; i < 5; i++) {
//            // 添加 key，score，value
//            jedis.zadd(key, System.currentTimeMillis(), value + i);
//            TimeUnit.SECONDS.sleep(1L);
//        }
        // 查询所有 value 带 score
        for (Tuple tuple : jedis.zrangeWithScores(key, 0, -1)) {
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(String.valueOf(BigDecimal.valueOf(tuple.getScore()))))));
        }
        System.out.println(jedis.zrangeWithScores(key, 0, -1));
        String[] a = new String[2];
        a[0] = Arrays.toString(jedis.zrange(key, 3, 3).toArray());
        a[1] = jedis.zrange(key, 3, 3).toArray()[0].toString();
//        System.out.println(a[0]);
//        System.out.println(a[1]);
        // 通过 key 和 score 获取 value
        System.out.println("通过 key 和 score 获取 value:" + jedis.zrangeByScore(key, 1632471636911L, 1632471636911L));
        // 删除排行指定区间所有value
//        jedis.zremrangeByRank(key, 0, 0);
//        jedis.zrangeWithScores(key, 0, -1).forEach(System.out::println);
        // 获取key中元素的个数
        System.out.println(jedis.zcard(key).toString());
        String date = "2021-08-19 12:00:00";
        long time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime();
        System.out.println(time);

    }
}
