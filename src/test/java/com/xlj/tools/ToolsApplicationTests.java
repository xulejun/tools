package com.xlj.tools;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.SystemUtil;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.Maps;
import com.xlj.tools.bean.QuaOcrHotelStartingPriceTaskDetail;
import com.xlj.tools.bean.Task;
import com.xlj.tools.bean.User;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjdk.jol.info.ClassLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.sun.management.OperatingSystemMXBean;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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

    static Kryo kryo;

    static {
        kryo = new Kryo();
        kryo.register(Task.class);
        kryo.register(QuaOcrHotelStartingPriceTaskDetail.class);
    }

    public static void main(String[] ars) throws Exception {

        String sourceData = "{\n" +
                "    \"type\": 2001011,\n" +
                "    \"buildTime\": 1678150800000,\n" +
                "    \"id\": 2,\n" +
                "    \"priority\": 10,\n" +
                "    \"taskDetail\": {\n" +
                "        \"needBuildByCity\": true,\n" +
                "        \"buildPath\": \"city\",\n" +
                "        \"province\": \"上海\",\n" +
                "        \"cityId\": \"310100\",\n" +
                "        \"cityName\": \"上海市\",\n" +
                "        \"hotelId\": \"50974020\",\n" +
                "        \"hotelName\": \"上海长荣桂冠酒店\",\n" +
                "        \"checkInDate\": \"2023-03-07\",\n" +
                "        \"checkOutDate\": \"2023-03-08\",\n" +
                "        \"hotelAddress\": \"祖冲之路1136号 ，近金科路\",\n" +
                "        \"type\": \"QuaOcrHotelStartingPriceTaskDetail\",\n" +
                "        \"resultType\": \"QuaOcrHotelStartingPriceResultDetail\",\n" +
                "        \"isundercarriage\": 0,\n" +
                "        \"canBooking\": 1,\n" +
                "        \"isLinkInvalidate\": -1,\n" +
                "        \"hotelStatusDesc\": \"可订\",\n" +
                "        \"laterPay\": \"true\"\n" +
                "    },\n" +
                "    \"level\": \"P0\"\n" +
                "}";
        System.out.println(sourceData);
        logger.info("String长度：{}，占用内存大小：{}", sourceData.length(), ClassLayout.parseInstance(sourceData).instanceSize());
        logger.info("String ：{}", ClassLayout.parseInstance(sourceData).toPrintable());
        byte[] bytes = sourceData.getBytes(StandardCharsets.UTF_8);
        logger.info("bytes 长度：{}，占用内存大小：{}", bytes.length, ClassLayout.parseInstance(bytes).instanceSize());
        logger.info("bytes ：{}", ClassLayout.parseInstance(sourceData).toPrintable());

        Task task = JSONUtil.toBean(sourceData, Task.class);
        logger.info("初始对象大小：{}", ClassLayout.parseInstance(sourceData).toPrintable());

        byte[] data = task.toString().getBytes();
        System.out.println(data);
        logger.info("对象转bytes：{}", ClassLayout.parseInstance(sourceData.getBytes()).toPrintable());

        JSONObject fastJson = (JSONObject) JSON.toJSON(task);
        byte[] fastJsonBytes = fastJson.toString().getBytes();
        logger.info("对象转 fastJsonBytes：{}，对象大小：{}", new String(fastJsonBytes), ClassLayout.parseInstance(fastJsonBytes).instanceSize());

        byte[] serialize = serialize(task);
        logger.info("kryo序列化后：{}，{}", new String(serialize), ClassLayout.parseInstance(serialize).instanceSize());

        byte[] compress = gzipCompress(data);
        logger.info("kryo序列化后压缩String：{}，压缩后大小：{}", new String(compress), ClassLayout.parseInstance(compress).instanceSize());

        Object object = deserialize(serialize, Task.class);
        logger.info("kryo反序列化后：{}，{}", object, ClassLayout.parseInstance(object.toString().getBytes()).instanceSize());

        byte[] decompress = gzipDecompress(compress);
        logger.info("解压后String：{}，解压后大小：{}", new String(decompress), ClassLayout.parseInstance(decompress).instanceSize());

    }

    /**
     * 文本数据gzip压缩
     */
    public static byte[] gzipCompress(byte[] data) {
        if (data.length == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(data);
            gzipOutputStream.flush();
            gzipOutputStream.finish();
        } catch (Exception e) {
            logger.warn("", e);
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 文本数据gzip解压
     *
     * @return
     */
    public static byte[] gzipDecompress(byte[] data) {
        if (data.length == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            logger.warn("", e);
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * kryo 序列化
     *
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);
        // 写入null时会报错
        kryo.writeObject(output, object);
        output.close();
        return bos.toByteArray();
    }

    /**
     * kryo 反序列化
     *
     * @param bytes
     * @return
     */
    public static Object deserialize(byte[] bytes, Class classType) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Input input = new Input(bis);
        // 读出null时会报错
        Object object = kryo.readObject(input, classType);
        input.close();
        return object;
    }


}


