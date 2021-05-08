package com.xlj.tools.hutool;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;

/**
 * hutool数字随机工具的使用
 *
 * @author xlj
 * @date 2021/5/8 10:32
 */
public class NumberUse {
    public static void main(String[] args) {
        // 判断是否为数字
        String s1 = "3.1415926";
        boolean integer = NumberUtil.isInteger(s1);
        System.out.println(integer);

        // 自动生成10个随机数
        int[] random = NumberUtil.generateRandomNumber(1, 1000, 10);
        System.out.println(Convert.toStr(random));

        // 精确的减法计算
        double result2 = NumberUtil.sub(1.2, 0.4);
        System.out.println(result2);

        // 四舍五入
        double result1 = NumberUtil.round(100.12356, 3).doubleValue();
        System.out.println(result1);

        System.out.println("随机获取范围内的一个整数:" + RandomUtil.randomInt(1, 1000));

        System.out.println("随机获取一个长度为10的字符串：" + RandomUtil.randomString(10));

        System.out.println("随机获取一个UUID:" + UUID.randomUUID().toString());

        System.out.println("随机获取一个简化的UUID：" + UUID.randomUUID().toString(true));
    }
}
