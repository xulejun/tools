package com.xlj.tools.basis.patterns.single;

/**
 * 单例模式：静态内部类
 *
 * @author xlj
 * @date 2020/12/14 21:20
 */
public class StaticInternal {
    public static void main(String[] args) {
        Single7 instance = Single7.getInstance();
        Single7 instance1 = Single7.getInstance();
        System.out.println(instance == instance1);
    }
}

class Single7 {

    private Single7() {
    }

    /**
     * 静态内部类，该类中一个静态属性Single
     */
    private static class SingleInstance {
        private static final Single7 INSTANCE = new Single7();
    }

    /**
     * 提供静态的公有方法，直接返回SingleInstance.INSTANCE
     */
    public static synchronized Single7 getInstance() {
        return SingleInstance.INSTANCE;
    }
}