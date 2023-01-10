package com.xlj.tools.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 图片相似度比较
 *
 * @author legend xu
 * @date 2022/6/15
 */
public class PhotoSimilarUtil {
    private static Logger logger = LoggerFactory.getLogger(PhotoSimilarUtil.class);


    public static OkHttpClient OKHTTPCLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();

    public static void main(String[] args) throws Exception {
        String[] urls = {"https://dimg04.fx.ctripcorp.com/images/0BF4812000ac3x8w62864.jpg", "https://dimg04.fx.ctripcorp.com/images/0BF6f12000aeg339xC711.jpg"};

        getImageMetaDataList(urls);
    }

    /**
     * 通过图片url 获取元数据
     *
     * @param imageUrls
     * @return
     */
    public static Map<String, List<String>> getImageMetaDataList(String[] imageUrls) {
        Map<String, List<String>> imageMetaDataMap = Maps.newHashMap();
        HashMap<String, String> combinationFeatures = Maps.newHashMap();

        // 图片url 下载转换成 输入流，获取图片原信息
        for (int i = 0; i < imageUrls.length; i++) {
            System.out.println(imageUrls[i]);
            Request request = new Request.Builder().url(imageUrls[i].replace("https","http")).get().build();
            try (Response response = OKHTTPCLIENT.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    // 从图片中获取原信息
                    Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
                    // 单张图片元数据
                    for (Directory directory : metadata.getDirectories()) {
                        for (Tag tag : directory.getTags()) {
                            String description = tag.getDescription();

                            String format = MessageFormat.format("[{0}]-{1}:{2}", directory.getName(), tag.getTagName(), tag.getDescription());
                            System.out.println(format);
                            String tagName = tag.getTagName();
                            // 元数据存储
                            List<String> metaValue = imageMetaDataMap.get(tagName);
                            if (metaValue == null) {
                                metaValue = Lists.newArrayList();
                            }
                            if (i != metaValue.size()) {
                                for (int j = 0; j < i - metaValue.size(); j++) {
                                    metaValue.add(";");
                                }
                            }
                            metaValue.add(tag.getDescription() + ";");
                            imageMetaDataMap.put(tagName, metaValue);

                            // 紧急处理组合特征值
                            if ("Image Width".equals(tag.getTagName()) && "1080".equals(description)) {
                                combinationFeatures.put(tag.getTagName(), description);
                            } else if ("Image Height".equals(tag.getTagName()) && "2400".equals(description)) {
                                combinationFeatures.put(tag.getTagName(), description);
                            } else if ("Color Type".equals(tag.getTagName()) && "True Color with Alpha".equals(description)) {
                                combinationFeatures.put(tag.getTagName(), description);
                            } else if ("sRGB Rendering Intent".equals(tag.getTagName()) && "Perceptual".equals(description)) {
                                combinationFeatures.put(tag.getTagName(), description);
                            } else if ("Significant Bits".equals(tag.getTagName()) && "8 8 8 8".equals(description)) {
                                combinationFeatures.put(tag.getTagName(), description);
                            } else if ("Detected File Type Name".equals(tag.getTagName()) && "PNG".equals(description)) {
                                combinationFeatures.put(tag.getTagName(), description);
                            }
                        }
                    }
                } else {
                    logger.warn("图片链接请求失败");
                }
            } catch (Exception e) {
                logger.warn("采集图片元信息异常：", e);
            }
        }

        combinationFeatures.forEach((key, value) -> {
            System.out.println(key + ":" + value);
        });
        System.out.println(combinationFeatures.size());
        return imageMetaDataMap;
    }


    /**
     * 获取图片原信息
     *
     * @throws Exception
     */
    public static void getPhotoMeta(String path) throws Exception {
        File file = new File(path);
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        for (Directory next : metadata.getDirectories()) {
            for (Tag tag : next.getTags()) {
                String format = MessageFormat.format("[{0}]-{1}:{2}", next.getName(), tag.getTagName(), tag.getDescription());
                System.out.println(format);
            }
        }
    }

    /**
     * 图片比较
     *
     * @param s
     * @param t
     * @return
     */
    public static float compare(int[] s, int[] t) {
        // 高于 70% 几乎可以认为是经过ps的图片
        try {
            float result = 0F;
            for (int i = 0; i < 256; i++) {
                int abs = Math.abs(s[i] - t[i]);
                int max = Math.max(s[i], t[i]);
                result = result + (1 - ((float) abs / (max == 0 ? 1 : max)));
            }
            return (result / 256) * 100;
        } catch (Exception exception) {
            return 0;
        }
    }

    /**
     * 图片转为数组
     *
     * @param name
     * @return
     */
    public static int[] getData(String name) {
        try {
            BufferedImage img = ImageIO.read(new File(name));
//            BufferedImage slt = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//            slt.getGraphics().drawImage(img, 0, 0, 100, 100, null);
            int[] data = new int[256];
            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    int rgb = img.getRGB(x, y);
                    Color myColor = new Color(rgb);
                    int r = myColor.getRed();
                    int g = myColor.getGreen();
                    int b = myColor.getBlue();
                    data[(r + g + b) / 3]++;
                }
            }
            // data 就是所谓图形学当中的直方图的概念
            return data;
        } catch (Exception exception) {
            System.out.println("有文件没有找到,请检查文件是否存在或路径是否正确");
            return null;
        }
    }

    /**
     * 差值哈希算法
     *
     * @param path
     * @return
     */
    public static char[] dHash(String path) throws Exception {
        // 图片转成流
        File file1 = new File(path);
        FileInputStream fis1 = new FileInputStream(file1);
        BufferedImage src = ImageIO.read(fis1);
        // 定义图片尺寸大小
        int width = 9;
        int height = 8;
        // 改变图⽚尺⼨
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.createGraphics();
        graphics.drawImage(src, 0, 0, width, height, null);

        int[] ints = new int[width * height];
        int index = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = image.getRGB(j, i);
                // 简化色彩
                // 将最⾼位（24-31）的信息（alpha通道）存储到a变量
                int a = pixel & 0xff000000;
                // 取出次⾼位（16-23）红⾊分量的信息
                int r = (pixel >> 16) & 0xff;
                // 取出中位（8-15）绿⾊分量的信息
                int g = (pixel >> 8) & 0xff;
                // 取出低位（0-7）蓝⾊分量的信息
                int b = pixel & 0xff;
                // NTSC luma，算出灰度值
                pixel = (r * 77 + g * 151 + b * 28) >> 8;
                // 将灰度值送⼊各个颜⾊分量
                int gray = a | (pixel << 16) | (pixel << 8) | pixel;
                ints[index++] = gray;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width - 1; j++) {
                if (ints[9 * j + i] >= ints[9 * j + i + 1]) {
                    builder.append(1);
                } else {
                    builder.append(0);
                }
            }
        }
        return builder.toString().toCharArray();
    }

    /**
     * 计算汉明距离
     *
     * @param c1
     * @param c2
     * @return
     */
    public static int diff(char[] c1, char[] c2) {
        int diffCount = 0;
        for (int i = 0; i < c1.length; i++) {
            if (c1[i] != c2[i]) {
                diffCount++;
            }
        }

        return diffCount;
    }
}
