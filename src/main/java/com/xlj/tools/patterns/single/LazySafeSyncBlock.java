package com.xlj.tools.patterns.single;

/**
 * 懒汉式（线程安全、同步代码块）
 *
 * @author xlj
 * @date 2020/12/10 22:38
 */
public class LazySafeSyncBlock {
    public static void main(String[] args) {
        Single5 instance = Single5.getInstance();
        Single5 instance1 = Single5.getInstance();
        System.out.println(instance == instance1);
    }
}

class Single5 {
    private static Single5 instance;

    private Single5() {

    }

    /**
     * 提供一个静态的公有方法，同步代码块锁对象，无法解决线程安全问题，进入if判断创建多个实例
     *
     * @return
     */
    public static Single5 getInstance() {
        if (instance == null) {
            synchronized (Single5.class) {
                instance = new Single5();
            }
        }
        return instance;
    }
}