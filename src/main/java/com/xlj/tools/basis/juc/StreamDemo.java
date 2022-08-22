package com.xlj.tools.basis.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        // filter 过滤（返回还是对象）
        list.stream().filter(t -> {
            return t.getId() % 2 == 0;
        }).forEach(System.out::println);

        // match：anyMatch、allMatch、noneMatch 对流中的元素进行匹配
        boolean y = list.stream().anyMatch(personBo -> {
            return personBo.getName().contains("y");
        });

        // map 将集合中的元素类型，转换成另一种数据类型
        list.stream().map(PersonBo::getId).forEach(System.out::println);

        // sorted 根据字段属性进行排序
        list.stream().sorted((u1, u2) -> {
            return u2.getId().compareTo(u1.getId());
        }).map(PersonBo::getId).forEach(System.out::println);

        // limit限制数————collect将map单个映射转为list集合
        list.stream().map(t -> {
            return t.getId();
        }).limit(1).forEach(System.out::println);

        // distinct 对流中的元素进行去重
        list.stream().distinct().forEach(System.out::println);

        // partitioningBy 根据判断的值为true还是false分为两组
        Map<Boolean, List<PersonBo>> collect = list.stream().collect(Collectors.partitioningBy(personBo -> {
            return personBo.getId() > 3;
        }));

        // groupingBy 将数据分组成多个key的形式（即groupby分组）
        Map<Integer, List<PersonBo>> collect1 = list.stream().collect(Collectors.groupingBy(PersonBo::getId));

        // reduce 计算集合总数
        Integer sum = list.stream().map(PersonBo::getId).reduce(Integer::sum).get();
        System.out.println(sum);
    }
}
