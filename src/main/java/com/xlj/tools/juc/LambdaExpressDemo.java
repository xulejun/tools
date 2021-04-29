package com.xlj.tools.juc;

/**
 * lambda表达式样例
 *
 * @author xlj
 * @date 2020/10/17 15:29
 */

/**
 * @description 只有一个方法的接口，称为函数式接口
 * @author xlj
 * @date 2020/10/17 16:35
 */
@FunctionalInterface    // 默认自带该注解（函数式接口）
interface Foo {
//    void sayHello();

    int calculate(int x, int y);

    default int mv(int x, int y) {
        System.out.println("Java8默认接口方法可以有实现方法体：" + x * y);
        return x * y;
    }

    static int mv2(int x, int y) {
        System.out.println("Java8接口方法可以有静态实现方法体：" + x / y);
        return x / y;
    }
}

public class LambdaExpressDemo {
    public static void main(String[] args) {
        // 正常写法
//        Foo foo = new Foo() {
//            @Override
//            public void sayHello() {
//                System.out.println("Hello");
//            }
//        };
        // lambda表达式写法-只有一个方法
//        Foo foo = () -> {
//            System.out.println("hello");
//        };
//        foo.sayHello();

        // lambda表达式写法-带有参数返回值
        Foo foo = (int x, int y) -> {
            System.out.println("calculate:" + (x + y));
            return x + y;
        };
        foo.calculate(5, 3);
        foo.mv(5, 3);
        Foo.mv2(5, 3);
    }
}
