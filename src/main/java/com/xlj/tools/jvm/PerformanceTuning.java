package com.xlj.tools.jvm;

import java.util.Random;

/**
 * 性能调优
 *
 * @author xlj
 * @date 2020/11/4 22:35
 */
public class PerformanceTuning {
    public static void main(String[] args) {
        // 当前计算机核心数
        System.out.println(Runtime.getRuntime().availableProcessors());

        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        System.out.println("-Xmx：默认最大内存：" + maxMemory + " byte = " + ((double) maxMemory / 1024 / 1024) + "MB");
        System.out.println("-Xms：默认初始内存：" + totalMemory + " byte = " + ((double) totalMemory / 1024 / 1024) + "MB");

//        testOutOfMemory();

        // 一次性干爆JVM,设置的JVM内存大小应小于40M
//        byte[] bytes = new byte[40 * 1024 * 1024];
    }

    /**
     * @description 堆内存溢出测试
     * 运行前在VM -options中设置JVM堆内存的大小并打印（初始值和最大值保持一致，内存忽高忽低，容易停顿）：-Xms1024m -Xmx1024m -XX:+PrintGCDetails
     * @author xlj
     * @date 2020/11/4 23:13
     */
    public static void testOutOfMemory() {
        String str = "";
        // 通过持续创建对象干爆JVM堆内存
        while (true) {
            str += str + new Random().nextInt(88888888) + new Random().nextInt(9999);
        }
    }
}
