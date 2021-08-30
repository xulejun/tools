package com.xlj.tools.basis.juc;

import java.util.ArrayList;
import java.util.List;

/**
 * Stream流式计算------可代替数据库查询
 * 与函数式接口紧密相关
 *
 * @author XLJ
 * @date 2020/10/26
 */
public class StreamDemo {
    public static void main(String[] args) {
        PersonBo personBo1 = new PersonBo(1, "xlj");
        PersonBo personBo2 = new PersonBo(2, "jyj");
        PersonBo personBo3 = new PersonBo(3, "XLJ");
        PersonBo personBo4 = new PersonBo(4, "JYJ");

        List<PersonBo> list = new ArrayList<>();
        list.add(personBo1);
        list.add(personBo2);
        list.add(personBo3);
        list.add(personBo4);

        // 集合stream流式计算————filter过滤（返回还是对象）
        list.stream().filter(t -> {
            return t.getId() % 2 == 0;
        }).forEach(System.out::println);

//        // 集合stream流式计算————map映射（返回为单个值）
//        list.stream().map(t -> {return t.getId();}).forEach(System.out::println);
//
//        // limit限制数————collect将map单个映射转为list集合
//        System.out.println(list.stream().map(t -> {
//            return t.getId();
//        }).limit(1).collect(Collectors.toList()));

        // reduce 计算集合总数
        Integer sum = list.stream().map(PersonBo::getId).reduce(Integer::sum).get();
        System.out.println(sum);
    }
}
