package com.xlj.tools.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xlj.tools.bean.Task;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.*;

/**
 * 压缩工具类
 * 1. 调研 世面常用的压缩算法/工具，综合压缩率和性能（压缩、解压耗时），gzip 和 deflater 为最佳
 * 1.1 不考虑耗时，lzma 压缩率最好，也最为耗时
 * 2. bzip2, gzip, pack200, lzma, xz, Snappy, DEFLATE, LZ4, Brotli, Zstandard（zstd） 均可在 org.apache.commons.common-compress 下有对应解压工具
 *
 * @author legend xu
 * @date 2023/3/10
 */
@Slf4j
public class CompressUtil {
    static int count = 10000;

    public static void main(String[] args) {
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
        Task task = JSONUtil.toBean(sourceData, Task.class);
        JSONObject json = (JSONObject) JSON.toJSON(task);
        byte[] bytes = json.toString().getBytes();
        log.info("对象的 bytes 大小：{} \t 对象 String：{}", task.toString().getBytes().length, task.toString());
        log.info("对象转 Json 后的大小：{}", json.toString().getBytes().length);

        TimeInterval timer = DateUtil.timer();
        int compressLength = 0;
        int decompressLength = 0;
        for (int i = 0; i < count; i++) {
            byte[] compress = gzipCompress(bytes);
            byte[] decompress = gzipDecompress(compress);
            compressLength = compress.length;
            decompressLength = decompress.length;
        }
        log.info("gzip 压缩 / 解压次数：{}，耗时：{}", count, timer.intervalRestart());
        log.info("gzip 压缩率为：{} \t 压缩后的大小：{} \t 解压后的大小：{}",
                NumberUtil.div(bytes.length - compressLength, bytes.length, 4), compressLength, decompressLength);

        int deflaterCompressLength = 0;
        int deflaterDecompressLength = 0;
        for (int i = 0; i < count; i++) {
            byte[] deflaterCompress = deflaterCompress(bytes);
            byte[] deflaterDecompress = deflaterDecompress(deflaterCompress);
            deflaterCompressLength = deflaterCompress.length;
            deflaterDecompressLength = deflaterDecompress.length;
        }
        log.info("deflater 压缩 / 解压次数：{}，耗时：{}", count, timer.intervalRestart());
        log.info("deflater 压缩率为：{} \t 压缩后的大小：{} \t 解压后的大小：{}",
                NumberUtil.div(bytes.length - deflaterCompressLength, bytes.length, 4), deflaterCompressLength, deflaterDecompressLength);
    }

    /**
     * 文本数据 gzip 压缩
     */
    public static byte[] gzipCompress(byte[] data) {
        if (data.length == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream outputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            outputStream.write(data);
            outputStream.flush();
            outputStream.finish();
        } catch (Exception e) {
            log.warn("", e);
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
        try (GZIPInputStream inputStream = new GZIPInputStream(byteArrayInputStream)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.warn("", e);
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 文本数据 Deflater 压缩
     */
    public static byte[] deflaterCompress(byte[] data) {
        // Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        Deflater deflater = new Deflater(9);
        if (data.length == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        deflater.setInput(data);
        deflater.finish();
        byte[] bytes = new byte[2048];
        while (!deflater.finished()) {
            int count = deflater.deflate(bytes);
            byteArrayOutputStream.write(bytes, 0, count);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 文本数据 Deflater 解压
     *
     * @return
     */
    public static byte[] deflaterDecompress(byte[] data) {
        if (data.length == 0) {
            return null;
        }
        Inflater inflater = new Inflater();
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            inflater.setInput(data);
            byte[] bytes = new byte[2048];
            while (!inflater.finished()) {
                int count = inflater.inflate(bytes);
                byteArrayOutputStream.write(bytes, 0, count);
            }
        } catch (DataFormatException e) {
            log.warn("", e);
        } finally {
            inflater.end();
        }
        return byteArrayOutputStream.toByteArray();
    }
}
