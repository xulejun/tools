package com.xlj.tools.basis.patterns.single;

/**
 * 单例模式：饿汉式（静态代码块）
 *
 * @author xlj
 * @date 2020/12/10 22:18
 */
public class HungryStaticBlock {
    public static void main(String[] args) {
        Single2 instance = Single2.getInstance();
        Single2 instance1 = Single2.getInstance();
        System.out.println(instance==instance1);
    }
}

class Single2 {
    /**
     * 1.私有化构造方法，防止外部实例化
     */
    private Single2() {

    }
    /**
     * 2.本类内部创建对象实例
     */
    private static Single2 instance;
    /**
     * 3.在静态代码块中创建单例对象
     */
    static {
        instance = new Single2();
    }
    /**
     * 4.提供公有的静态方法，返回实例对象
     * @return
     */
    public static Single2 getInstance() {
        return instance;
    }
}
