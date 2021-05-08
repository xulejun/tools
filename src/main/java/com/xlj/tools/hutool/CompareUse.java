package com.xlj.tools.hutool;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.PropertyComparator;
import cn.hutool.core.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * hutool比较工具的使用
 *
 * @author xlj
 * @date 2021/5/8 10:59
 */
public class CompareUse {
    public static void main(String[] args) {
        // 根据对象属性进行集合排序
        List<Hero> heros = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            heros.add(new Hero("hero " + i, RandomUtil.randomInt(100)));
        }
        System.out.println("未排序的集合:");
        System.out.println(CollectionUtil.join(heros, "\r\n"));

        Collections.sort(heros, new PropertyComparator<>("hp"));
        System.out.println("根据属性 hp 排序之后：");
        System.out.println(CollectionUtil.join(heros, "\r\n"));
    }


    @Data
    @AllArgsConstructor
    static class Hero {
        String name;
        int hp;
    }
}
