package com.xlj.tools;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ToolsApplicationTests {
    private static Logger logger = LoggerFactory.getLogger(ToolsApplicationTests.class);

    public static OkHttpClient OKHTTPCLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();


    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void methodTest() throws Exception {
        long INTERVAL = 600;
        // 时间间隔内调用的次数
        redisTemplate.opsForHash().increment("sendMailKey", "count", 1L);
        // 当天内调用的次数
        redisTemplate.opsForHash().increment("sendMailKey", "totalCount", 1L);
        // 最后一次发送邮件的时间
        String timeStamp = (String) redisTemplate.opsForHash().get("sendMailKey", "timeStamp");
        long lastSendTime = StringUtils.isBlank(timeStamp) ? 0 : Long.parseLong(timeStamp);
        // 满足时间间隔，则发送邮件通知
        if (System.currentTimeMillis() > lastSendTime + INTERVAL * 1000) {
            Object count = redisTemplate.opsForHash().get("sendMailKey", "count");
            Object totalCount = redisTemplate.opsForHash().get("sendMailKey", "totalCount");
            // 发送邮件
            log.info("邮件发送,count={},totalCount={}", count, totalCount);
            // 重置
            redisTemplate.opsForHash().put("sendMailKey", "count", 1);
            redisTemplate.opsForHash().put("sendMailKey", "timeStamp", String.valueOf(System.currentTimeMillis()));
            LocalDate localDate = LocalDateTime.now().plusDays(1).toLocalDate();
            // 当天24点的时间戳（秒）
            long expireTimeSeconds = LocalDateTime.of(localDate.getYear(), localDate.getMonth(),
                    localDate.getDayOfMonth(), 0, 0).toEpochSecond(ZoneOffset.of("+8"));
            redisTemplate.expire("sendMailKey", expireTimeSeconds, TimeUnit.SECONDS);
        }
    }

    public static void main(String[] ars) throws Exception {

    }
}


