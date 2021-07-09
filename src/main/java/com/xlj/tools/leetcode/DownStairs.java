package com.xlj.tools.leetcode;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;

/**
 * 力扣：下楼梯
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 *
 * @author xlj
 * @date 2021/7/9
 */
public class DownStairs {

    /**
     * 用递归的方式，时间复杂度太高
     *
     * @param n
     * @return
     */
    public static int climbStairs(int n) {
        if (n <= 2) {
            return n;
        } else {
            return climbStairs(n - 1) + climbStairs(n - 2);
        }
    }

    public static int climbStairsByFor(int n) {
        if (n <= 2) {
            return n;
        }
        int x = 1;
        int y = 2;
        int result = 0;
        for (int i = 2; i < n; i++) {
            result = x + y;
            x = y;
            y = result;
        }
        return result;
    }

    public static void main(String[] args) {
        TimeInterval timer = DateUtil.timer();
        System.out.println(climbStairsByFor(45));
        System.out.println("循环实现耗时：" + timer.interval());
//        System.out.println(climbStairs(45));
//        System.out.println("递归实现耗时：" + timer.interval());

    }
}
