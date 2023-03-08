package com.xlj.tools;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.xlj.tools.bean.QuaOcrHotelStartingPriceTaskDetail;
import com.xlj.tools.bean.Task;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import okhttp3.OkHttpClient;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
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
import org.xerial.snappy.Snappy;
import org.zeroturnaround.zip.ZipUtil;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;
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

    static ZipParameters zip4jParameters;

    static {
        zip4jParameters = new ZipParameters();
        zip4jParameters.setFileNameInZip("test");
        zip4jParameters.setCompressionMethod(CompressionMethod.DEFLATE); // 压缩方式
        zip4jParameters.setCompressionLevel(CompressionLevel.ULTRA); // 压缩级别
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
//                "        \"shareShortLink\": \"qconfig.ctripcorp.com/webapp/page/index.html#/qconfig/null/uat:?groupName=null\",\n" +
//                "        \"sharePassword\": \"12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678900" +
//                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\",\n" +
                "        \"isundercarriage\": 0,\n" +
                "        \"canBooking\": 1,\n" +
                "        \"isLinkInvalidate\": -1,\n" +
                "        \"hotelStatusDesc\": \"可订\",\n" +
                "        \"laterPay\": \"true\"\n" +
                "    },\n" +
                "    \"level\": \"P0\"\n" +
                "}";

        // logger.info("String 大小：{}", sourceData.length());
        // logger.info("String 转 bytes 大小：{}", bytes.length);

        Task task = JSONUtil.toBean(sourceData, Task.class);
        byte[] bytes = task.toString().getBytes();
        logger.info("对象的 bytes 大小：{}", bytes.length);
        JSONObject json = (JSONObject) JSON.toJSON(task);
        logger.info("对象转 Json 后的大小：{}", json.toString().getBytes().length);
        logger.info("转成 Json 后再 gzip 压缩后的大小：{}", gzipCompress(json.toString().getBytes()).length);

        byte[] serialize = serialize(task);
        logger.info("kryo 序列化后的大小：{}", serialize.length);
        Object object = deserialize(serialize, Task.class);
        logger.info("kryo 反序列化后的大小：{}", object.toString().getBytes().length);

        byte[] compress = gzipCompress(bytes);
        logger.info("gzip 压缩后的大小：{}", compress.length);
        byte[] decompress = gzipDecompress(compress);
        logger.info("gzip 解压后的大小：{}", decompress.length);

        byte[] zipCompress = zip4jCompress(bytes);
        logger.info("zip4j 压缩后的大小：{}", zipCompress.length);
        byte[] zipDecompress = zip4jDecompress(zipCompress);
        logger.info("zip4j 解压后的大小：{}", zipDecompress.length);

        byte[] snappyCompress = snappyCompress(bytes);
        logger.info("snappy 压缩后的大小：{}", snappyCompress.length);
        byte[] snappyDecompress = snappyDecompress(snappyCompress);
        logger.info("snappy 解压后的大小：{}", snappyDecompress.length);

        byte[] commonsCompress = commonsCompress(bytes);
        logger.info("commonsCompress 压缩后的大小：{}", commonsCompress.length);
        byte[] commonsDecompress = commonsDecompress(commonsCompress);
        logger.info("commonsCompress 解压后的大小：{}", commonsDecompress.length);

        byte[] jzLibCompress = jzLibCompress(bytes);
        logger.info("jzLibCompress 压缩后的大小：{}", jzLibCompress.length);
        byte[] jzLibDecompress = jzLibDecompress(jzLibCompress);
        logger.info("commonsCompress 解压后的大小：{}", jzLibDecompress.length);


    }

    /**
     * 文本数据 jzlib 压缩
     */
    public static byte[] jzLibCompress(byte[] data) {
        if (data.length == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream outputStream = new GZIPOutputStream(byteArrayOutputStream);) {
            outputStream.write(data);
            outputStream.flush();
            outputStream.finish();
        } catch (Exception e) {
            logger.warn("", e);
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 文本数据 jzlib 解压
     *
     * @return
     */
    public static byte[] jzLibDecompress(byte[] data) {
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
     * 文本数据 commons-compress 压缩
     */
    public static byte[] commonsCompress(byte[] data) {
        if (data.length == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ArArchiveOutputStream outputStream = new ArArchiveOutputStream(byteArrayOutputStream);) {
            outputStream.write(data);
            outputStream.flush();
            outputStream.finish();
        } catch (Exception e) {
            logger.warn("", e);
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 文本数据 commons-compress 解压
     *
     * @return
     */
    public static byte[] commonsDecompress(byte[] data) {
        if (data.length == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        try (ZipArchiveInputStream gzipInputStream = new ZipArchiveInputStream(byteArrayInputStream)) {
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
     * 文本数据 snappy 压缩
     *
     * @param data
     * @return
     */
    public static byte[] snappyCompress(byte[] data) throws IOException {
        return Snappy.compress(data);
    }

    /**
     * 文本数据gzip解压
     *
     * @return
     */
    public static byte[] snappyDecompress(byte[] data) throws IOException {
        return Snappy.uncompress(data);
    }

    /**
     * 文本数据 zip4j 压缩
     *
     * @param data
     * @return
     */
    public static byte[] zip4jCompress(byte[] data) {
        if (data.length == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream outputStream = new ZipOutputStream(byteArrayOutputStream)) {
            outputStream.putNextEntry(zip4jParameters);
            outputStream.write(data);
            outputStream.flush();
        } catch (Exception e) {
            logger.warn("", e);
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 文本数据 zip4j 解压
     *
     * @return
     */
    public static byte[] zip4jDecompress(byte[] data) {
        if (data.length == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        try (ZipInputStream inputStream = new ZipInputStream(byteArrayInputStream)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            logger.warn("", e);
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 文本数据 gzip 压缩
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
     * 文本数据 gzip 解压
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


