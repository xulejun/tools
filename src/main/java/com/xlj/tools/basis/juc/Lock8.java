package com.xlj.tools.basis.juc;

import java.util.concurrent.TimeUnit;

/**
 * @author XLJ
 * @date 2020/10/19
 */
class Phone{
    public synchronized void print1(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("1");
    }
    public  synchronized void print2(){
        System.out.println("2");
    }
    public void hello(){
        System.out.println("hello");
    }
}

/**
 * @description: 多线程8锁
 * 1.标准访问，先输出1还是输出2？                                        answer：按顺序访问，1   2
 * 2.1方法暂停1秒，先输出1还是输出2？                                    answer：方法同步，1方法暂停，2方法等待，1  2
 * 3.新增普通hello方法，先输出1还是输出hello？                           answer：hello方法不是同步方法，无需等待，hello 1
 * 4.两部手机，先输出1还是输出2？                                        answer：对象不同，无需等待方法1，2   1
 * 5.两个静态同步方法，同一部手机，先输出1还是输出2？                        answer：静态方法锁当前资源类.class文件，需等待，1  2
 * 6.两个静态同步方法，两部手机，先输出1还是输出2？                        answer：两个静态同步方法，锁当前资源类，1   2
 * 7.一个普通同步方法，一个静态同步方法，一部手机，先输出1还是输出2？        answer：静态同步方法，锁当前资源类，普通同步方法锁当前对象，锁的不同，2   1
 * 8.一个普通同步方法，一个静态同步方法，两部手机，先输出1还是输出2？        answer：静态同步方法，锁当前资源类，普通同步方法锁当前对象，锁的不同，2   1
 *
 * @author XLJ 
 * @date 2020/10/19 19:24
*/
public class Lock8 {
    public static void main(String[] args) throws InterruptedException {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(()->{
            phone1.print1();
        },"A").start();

//        Thread.sleep(1000);

        new Thread(()->{
//            phone2.print2();
            phone1.print2();
        },"B").start();
    }
}
