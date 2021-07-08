package com.xlj.tools.leetCode;

/**
 * 回文数：
 * 给你一个整数 x ，如果 x 是一个回文整数，返回 true ；否则，返回 false 。
 * 回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。例如，121 是回文，而 123 不是。
 *
 * @author xlj
 * @date 2021/7/7
 */
public class Palindrome {
    public static boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        boolean result = false;
        char[] chars = String.valueOf(x).toCharArray();
        int y = chars.length / 2;
        for (int i = 0; i <= y; i++) {
            result = chars[i] == chars[chars.length - i - 1];
            if (result == false) {
                break;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(isPalindrome(100));
    }
}
