package com.xlj.tools;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.xlj.tools.bean.User;
import com.xlj.tools.enums.InfoTypeEnum;
import com.xlj.tools.util.PhotoSimilarUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisMonitor;
import redis.clients.jedis.JedisPool;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
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

    private int a = 0;

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

    public static void main(String[] args) throws Exception {
        String urls = " https://dimg04.fx.ctripcorp.com/images/0BF6l120009nllg5p1214.jpg,https://dimg04.fx.ctripcorp.com/images/0BF1m120009nlluwb7809.jpg";
        String[] imageUrls = urls.replace("https:", "http:").split(",");
        PhotoSimilarUtil.getImageMetaDataList(imageUrls);
    }


    public void hello() throws InterruptedException {
        synchronized (this) {
            System.out.println(Thread.currentThread().getName() + "：开始执行");
//            TimeUnit.SECONDS.sleep(2);
            System.out.println(Thread.currentThread().getName() + "：执行结束");
        }
    }

    private static void fileRead(String path) throws IOException {
        File file = new File(path);
        ArrayList<String> list = new ArrayList<>();
//        ArrayList<String> username = new ArrayList<>();
        HashSet<String> photoType = new HashSet<>();
        BufferedReader bufferedReader = null;
        String str = null;
        bufferedReader = new BufferedReader(new FileReader(file));
        while ((str = bufferedReader.readLine()) != null) {
            list.add(str);
            if (str.contains("- Image Width =") && !str.contains("Exif")) {
                photoType.add(str.substring(str.indexOf("=") + 1, str.indexOf("=") + 4));
            }
            if (str.contains("--------------------------------------------------------------------")) {
                if (photoType.size() > 1) {
                    System.out.println("当前行号为：" + list.size());
//                    System.out.println("用户名为：" + username.get(0));
                }
                photoType.clear();
//                username.clear();
            }
        }
    }

    public static void printAllMetadata(InputStream inputStream) {
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(inputStream);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    String format = String.format("[%s] - %s = %s", directory.getName(), tag.getTagName(), tag.getDescription());
                    System.out.println(format);
                }
                if (directory.hasErrors()) {
                    for (String error : directory.getErrors()) {
                        String format = String.format("ERROR: %s", error);
                        System.out.println(format);
                    }
                }
            }
        } catch (ImageProcessingException e) {
            log.error("图片处理问题", e);
        } catch (IOException e) {
            log.error("图片IO问题", e);
        }
    }

}


