package com.xlj.tools.hutool;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.PropertyComparator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
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
        String str1 = "朝阳区安达街旺座国际商务广场斜对面（近轻轨3号线南昌路站）";
        String str2 = "朝阳区安袄街旺座国际商务广场斜对面(祈轻劲3 地图/导航号线南昌路站)吉大一院、文化广场";
        // 计算两个字符串的相似度
        double similar = StrUtil.similar(str1, str2);
        System.out.println("字符串相似度为：" + similar);

        // 根据对象属性进行集合排序
        List<Hero> heros = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            heros.add(new Hero("hero " + i, RandomUtil.randomInt(100)));
        }
//        System.out.println("未排序的集合:");
//        System.out.println(CollectionUtil.join(heros, "\r\n"));

        Collections.sort(heros, new PropertyComparator<>("hp"));
//        System.out.println("根据属性 hp 排序之后：");
//        System.out.println(CollectionUtil.join(heros, "\r\n"));
    }


    @Data
    @AllArgsConstructor
    static class Hero {
        String name;
        int hp;
    }
}
