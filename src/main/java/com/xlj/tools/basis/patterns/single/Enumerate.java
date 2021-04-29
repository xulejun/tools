package com.xlj.tools.basis.patterns.single;

/**
 * 单例模式：枚举
 *
 * @author xlj
 * @date 2020/12/14 21:40
 */
public class Enumerate {
    public static void main(String[] args) {
        Single8 instance = Single8.INSTANCE;
        Single8 instance1 = Single8.INSTANCE;
        System.out.println(instance == instance1);
        instance.say();
    }
}

enum Single8{
    /**
     * 枚举单例
     */
    INSTANCE;

    public void say(){
        System.out.println("hello");
    }
}
