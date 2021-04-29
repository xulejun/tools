package com.xlj.tools.basis.patterns.single;

/**
 * 懒汉式（线程不安全）
 *
 * @author xlj
 * @date 2020/12/10 22:38
 */
public class LazyNoSafe {
    public static void main(String[] args) {
        Single3 instance = Single3.getInstance();
        Single3 instance1 = Single3.getInstance();
        System.out.println(instance==instance1);
    }
}

class Single3 {
    private static Single3 instance;
    private Single3() {

    }
    /**
     * 提供一个静态的公有方法，当使用到该方法时，才去创建instance
     *
     * @return
     */
    public static Single3 getInstance() {
        if (instance == null) {
            instance = new Single3();
        }
        return instance;
    }
}