package com.xlj.tools.patterns.single;

/**
 * 双重检查
 *
 * @author xlj
 * @date 2020/12/10 22:38
 */
public class DoubleCheck {
    public static void main(String[] args) {
        Single6 instance = Single6.getInstance();
        Single6 instance1 = Single6.getInstance();
        System.out.println(instance == instance1);
    }
}

class Single6 {
    /**
     * 增加volatile多线程可见性关键字
     */
    private static volatile Single6 instance;
    private Single6() {

    }
    /**
     * 提供一个静态的公有方法，利用同步代码块进行双重检查，提高效率，解决线程安全的问题
     *
     * @return
     */
    public static Single6 getInstance() {
        if (instance == null) {
            synchronized (Single6.class) {
                if (instance == null) {
                    instance = new Single6();
                }
            }
        }
        return instance;
    }
}