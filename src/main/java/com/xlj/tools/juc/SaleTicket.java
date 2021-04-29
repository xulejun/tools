package com.xlj.tools.juc;

/**
 * 多线程售票
 *
 * @author xlj
 * @date 2020/10/17 14:51
 */
public class SaleTicket {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        // lambda表达式方式实现
        new Thread(()->{ for (int i = 0; i < 40; i++)ticket.sale();},"A").start();
        new Thread(()->{ for (int i = 0; i < 40; i++)ticket.sale();},"B").start();
        new Thread(()->{ for (int i = 0; i < 40; i++)ticket.sale();},"C").start();

        // 匿名内部类的方式实现
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 40; i++) {
//                    ticket.sale();
//                }
//            }
//        }, "A").start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 40; i++) {
//                    ticket.sale();
//                }
//            }
//        }, "B").start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 40; i++) {
//                    ticket.sale();
//                }
//            }
//        }, "C").start();
    }
}

/**
* @description 售票实体类
* @author xlj
* @date 2020/10/17 15:09
*/
class Ticket {
    public int votes = 30;

    public synchronized void sale() {
        if (votes > 0) {
            System.out.println(Thread.currentThread().getName() + "卖出第" + votes-- + "张票，\t还剩余" + votes + "张票");
        }
    }
}