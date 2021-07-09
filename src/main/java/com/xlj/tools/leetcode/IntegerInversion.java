package com.xlj.tools.leetcode;


import java.text.MessageFormat;

/**
 * 力扣：整数反转
 * 给你一个 32 位的有符号整数 x ，返回将 x 中的数字部分反转后的结果。
 * 如果反转后整数超过 32 位的有符号整数的范围 [−231,  231 − 1] ，就返回 0
 *
 * @author xlj
 * @date 2021/7/7
 */
public class IntegerInversion {

    /**
     * 调用StringBuilder的reverse方法
     *
     * @param i
     * @return
     */
    public static int reverse(int i) {
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(i)).reverse();
        return Integer.parseInt(String.valueOf(stringBuilder));
    }

    public static long array(int x) {
        // 数值大小校验
        if (x > Math.pow(2d, 31d) - 1 || x < -Math.pow(2d, 31d)) {
            return 0;
        }
        String value = String.valueOf(x);
        char[] chars;
        StringBuilder builder = new StringBuilder();
        if (value.contains("-")) {
            chars = value.substring(value.indexOf("-") + 1).toCharArray();
            builder.append("-");
        } else {
            chars = value.toCharArray();
        }
        for (int i = chars.length - 1; i >= 0; i--) {
            builder.append(chars[i]);
        }
        return Long.valueOf(builder.toString());
    }

    /**
     * 数值计算
     *
     * @param x
     * @return
     */
    public static int caculate(int x) {
        long n = 0;
        while (x != 0) {
            n = n * 10 + x % 10;
            x = x / 10;
        }
        return (int) n == n ? (int) n : 0;
    }

    public static void main(String[] args) {
        int x = 153423;
//        int result = reverse(x);
//        long result = array(x);
        int result = caculate(x);
        System.out.println(MessageFormat.format("转换前：{0}，转换后：{1}", x, result));
    }
}
