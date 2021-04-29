package com.xlj.tools.basis.juc;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 举例说明：集合类是不安全的
 * 1.故障现象：java.util.ConcurrentModificationException
 * 2.解决方案：
 * 2.1 Vector线程安全（add方法有synchronize修饰）
 * 2.2 Collections.synchronizedList(new ArrayList<>())
 * 2.3 new CopyOnWriteArrayList<>()    在JUC包下
 * 3. 原理
 * CopyOnWriteArrayList中的add方法，采用的也是读写分离的思想，读和写不同的容器，先将之前的容器进行复制，然后再添加新元素
 *
 * @author XLJ
 * @date 2020/10/20
 */
public class NoSafeDemo {

    public static void main(String[] args) {
        // List 线程不安全
        listNoSafe();

        // Set线程不安全，HashSet底层数据结构是HashMap，add方法就是put方法，value是PRESENT静态常量
        setNoSafe();

        // Map线程不安全，ConcurrentHashMap解决该集合线程不安全的问题，HashMap默认容量为16，加载因子为0.75，
        // 数据结构为Node数组+Node链表+红黑树，超过16*0.75（12）进行扩容，容量为当前的一倍，2^5
        mapNoSafeMap();
    }

    public static void mapNoSafeMap() {
        Map<Object, Object> concurrentHashMap = new ConcurrentHashMap<>();  // Collections.synchronizedMap();    // new HashMap<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                concurrentHashMap.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0, 8));
                System.out.println(concurrentHashMap);
            }, String.valueOf(i)).start();
        }
    }

    public static void setNoSafe() {
        Set<Object> hashSet = new CopyOnWriteArraySet<>();      // Collections.synchronizedSet(); // new HashSet<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                hashSet.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(hashSet);
            }, String.valueOf(i)).start();
        }
    }

    public static void listNoSafe() {
        // 缩写
//        List<String> list = Arrays.asList("a", "b", "c");
//        list.forEach(System.out::println);
        List<String> list1 = new CopyOnWriteArrayList();   // Collections.synchronizedList(new ArrayList<>());   // new Vector<>();    // new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                list1.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list1);
            }, String.valueOf(i)).start();
        }
    }
}
