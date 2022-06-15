package com.xlj.tools.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

/**
 * 图片相似度比较
 *
 * @author legend xu
 * @date 2022/6/15
 */
public class PhotoSimilarUtil {
    public static void main(String[] args) throws Exception {
        String path = "D:\\Users\\lejunxu\\Desktop\\picture\\原图.jpg";
        String path1 = "D:\\Users\\lejunxu\\Desktop\\picture\\PS2021.jpg";

        // 获取图片原信息
//        getPhotoMeta(path);

        // 两种算法进行图片相似度比较
        // 第一种
        int[] data = getData(path);
        int[] data1 = getData(path1);
        float compare = compare(data, data1);
        // 高于 70% 几乎可以认为是经过ps的图片
        if (compare == 0) {
            System.out.println("无法比较");
        } else {
            System.out.println("两张图片的相似度为：" + compare + "%");
        }

        // 第二种
        // 计算图片的灰度差值
        char[] chars = dHash(path);
        char[] chars1 = dHash(path1);
        // 计算汉明距离
        int diff = diff(chars, chars1);
        // 汉明距离越小，越相似
        System.out.println("两张图片的汉明距离为：" + diff + "（汉明距离越小，越相似）");
    }


    /**
     * 获取图片原信息
     *
     * @throws Exception
     */
    private static void getPhotoMeta(String path) throws Exception {
        File file = new File(path);
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        for (Directory next : metadata.getDirectories()) {
            for (Tag tag : next.getTags()) {
                tag.getDescription();
                System.out.println(tag.getTagName() + ":" + tag.getDescription());
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
            BufferedImage slt = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            slt.getGraphics().drawImage(img, 0, 0, 100, 100, null);
            int[] data = new int[256];
            for (int x = 0; x < slt.getWidth(); x++) {
                for (int y = 0; y < slt.getHeight(); y++) {
                    int rgb = slt.getRGB(x, y);
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
