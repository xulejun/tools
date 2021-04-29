package com.xlj.tools.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author XLJ
 * @date 2020/10/13
 */
public class NoBlockingNIOTest2 {
    /**
     * @description: 发送端
     * @author XLJ
     * @date 2020/10/13 14:20
     */
    @Test
    public void send() throws IOException {
        // 1.创建数据通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        // 2.开启非阻塞模式
        datagramChannel.configureBlocking(false);
        // 3.创建非直接缓冲区，指定大小
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 4.数据发送
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            byteBuffer.put((new Date().toString() + "\n" + next).getBytes());
            byteBuffer.flip();
            datagramChannel.send(byteBuffer, new InetSocketAddress("127.0.0.1", 9898));
            byteBuffer.clear();
        }
        // 5.关闭
        datagramChannel.close();
    }

    @Test
    public void receive() throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
        datagramChannel.bind(new InetSocketAddress(9898));

        // 创建选择器
        Selector selector = Selector.open();
        // 通道注册到选择器上
        datagramChannel.register(selector, SelectionKey.OP_READ);

        // 轮询方式获取选择器上已准备就绪的事件
        while (selector.select() > 0) {
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()){
                // 获取准备就绪的事件
                SelectionKey next = keyIterator.next();
                // 判断准备就绪的条件
                if (next.isReadable()){
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                    datagramChannel.receive(byteBuffer);
                    byteBuffer.flip();
                    System.out.println(new String(byteBuffer.array(),0,byteBuffer.limit()));
                    byteBuffer.clear();
                }
            }
            keyIterator.remove();
        }
    }
}
