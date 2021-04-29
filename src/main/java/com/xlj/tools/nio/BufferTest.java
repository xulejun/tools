package com.xlj.tools.nio;


import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author xlj
 * @date 2020/10/11 12:56
 *
 * 缓冲区的两个核心方法：
 * put：存入数据到缓冲区内
 * get：获取缓冲区的数据
 */
public class BufferTest {

    @Test
    public void test1(){
        String str = "abcde";
        // 1.分配一个指定大小的非直接缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("----------allocate-------------");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        // 2.存入数据
        byteBuffer.put(str.getBytes());
        System.out.println("----------put-------------");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        // 3.flip切换读取数据模式
        byteBuffer.flip();
        System.out.println("----------flip-------------");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        // 4.利用get()读取缓冲区中的数据
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        System.out.println("----------get-------------");
        System.out.println(new String(bytes,0,bytes.length));
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        // 5.rewind可重复读
        byteBuffer.rewind();
        System.out.println("----------rewind-------------");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        // 6.clear：清空缓冲区。但是缓冲区中的数据依旧存在，只是处于一种被遗忘的状态
        byteBuffer.clear();
        System.out.println("----------clear-------------");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        System.out.println((char) byteBuffer.get());
    }
    @Test
    public void test2(){
        // ByteBuffer存储数据
        String str = "abcde";
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());

        // 转换成读模式
        byteBuffer.flip();
        // 放入到byte数组中
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes,0,2);
        System.out.println(new String(bytes,0,2));
        System.out.println(byteBuffer.position());
        // 标记当下的位置position
        byteBuffer.mark();

        byteBuffer.get(bytes,2,2);
        System.out.println(new String(bytes,2,2));
        System.out.println(byteBuffer.position());
        // 回到标记的位置
        byteBuffer.reset();
        System.out.println(byteBuffer.position());

        // 判断是否含有剩余的数据
        if (byteBuffer.hasRemaining()){
            System.out.println(byteBuffer.remaining());
        }
        System.out.println(new String(bytes,0,bytes.length));
    }

    @Test
    public void test3(){
        // 分配一个直接缓冲区
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(1024);
        System.out.println(allocateDirect.isDirect());
    }
}