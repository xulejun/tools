package com.xlj.tools.basis.juc;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 四大函数式接口样例
 *
 * @author xlj
 * @date 2020/10/25 16:44
 */
public class FunctionalInterfaceDemo {
    public static void main(String[] args) {
        // 函数型接口---------匿名内部类写法
//        Function<String, Integer> function = new Function<String, Integer>() {
//            @Override
//            public Integer apply(String s) {
//                return 1024;
//            }
//        };

        // 1.函数型接口---------lambda表达式写法
        Function<String, Integer> function = (String s) -> {
            return 1024;
        };
        System.out.println(function.apply("xlj"));

        // 2.断定型接口---------lambda表达式写法
        Predicate<String> predicate = (String s) -> {
            return !s.isEmpty();
        };
        System.out.println(predicate.test("xlj"));

        // 3.消费型接口---------lambda表达式写法
        Consumer<String> consumer = (String s) -> {
            System.out.println("消费型接口没有返回类型");
        };
        consumer.accept("xlj");

        // 4.供给型接口---------lambda表达式写法
        Supplier<String> supplier = () -> {
            return "xlj";
        };
        System.out.println(supplier.get());

    }
}
