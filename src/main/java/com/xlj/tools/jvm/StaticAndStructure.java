package com.xlj.tools.jvm;

/**
 * 静态代码块、构造块和构造方法的执行顺序
 * 主类静态代码块 > main方法 > 外部类静态代码块（只加载一次） > 外部类构造代码块 》 构造方法 》 主类构造块 》主类构造方法
 *
 * @author xlj
 * @date 2020/11/8 15:27
 */
class Code {
    private static int j = staticMethod();

    public Code() {
        System.out.println("外部类构造方法");
    }

    {
        System.out.println("外部类构造代码块");
    }

    static {
        System.out.println("外部类静态代码块");
    }

    private static int staticMethod() {
        System.out.println("外部类静态方法");
        return 1;
    }
}

// 静态和构造主类
public class StaticAndStructure {
    private static int j = staticMethod();

    public StaticAndStructure() {
        System.out.println("主类构造方法");
    }

    {
        System.out.println("主类构造块");
    }

    static {
        System.out.println("主类静态代码块");
    }

    private static int staticMethod() {
        System.out.println("主类静态方法");
        return 1;
    }

    public static void main(String[] args) {
        System.out.println("main方法");
        new Code();
        System.out.println("--------------------");
        new Code();
        System.out.println("--------------------");
        new StaticAndStructure();
        /*
        主类静态代码块
        main方法
        外部类静态代码块
        外部类构造代码块
        外部类构造方法
        --------------------
        外部类构造代码块
        外部类构造方法
        --------------------
        主类构造块
        主类构造方法
         */
    }
}
