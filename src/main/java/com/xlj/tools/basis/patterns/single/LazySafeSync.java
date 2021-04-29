package com.xlj.tools.basis.patterns.single;

/**
 * 懒汉式（线程安全、同步方法）
 *
 * @author xlj
 * @date 2020/12/10 22:38
 */
public class LazySafeSync {
    public static void main(String[] args) {
        Single4 instance = Single4.getInstance();
        Single4 instance1 = Single4.getInstance();
        System.out.println(instance==instance1);
    }
}

class Single4 {
    private static Single4 instance;
    private Single4() {

    }
    /**
     * 提供一个静态的公有方法，加入同步处理的代码，解决线程安全问题
     *
     * @return
     */
    public static synchronized Single4 getInstance() {
        if (instance == null) {
            instance = new Single4();
        }
        return instance;
    }
}