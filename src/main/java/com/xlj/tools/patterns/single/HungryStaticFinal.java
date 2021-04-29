package com.xlj.tools.patterns.single;

/**
 * 单例模式：饿汉式（静态常量）
 *
 * @author xlj
 * @date 2020/12/10 22:01
 */
public class HungryStaticFinal {
    public static void main(String[] args) {
        Single1 instance = Single1.getInstance();
        Single1 instance1 = Single1.getInstance();
        System.out.println(instance == instance1);
    }
}

class Single1 {
    /**
     * 1.无参构造私有化，防止外部实例
     */
    private Single1() {

    }

    /**
     * 2.本类内部创建实例
     */
    private static final Single1 instance = new Single1();

    /**
     * 3.提供公有的静态方法，返回实例对象
     *
     * @return
     */
    public static Single1 getInstance() {
        return instance;
    }
}