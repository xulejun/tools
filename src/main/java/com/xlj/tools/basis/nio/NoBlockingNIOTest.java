package com.xlj.tools.basis.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 非阻塞NIO
 *
 * @author XLJ
 * @date 2020/10/13
 */
public class NoBlockingNIOTest {
    /**
     * @description: 服务端
     * @author XLJ
     * @date 2020/10/13 14:02
    */
    @Test
    public void server() throws IOException {
        // 1.获取服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 2.切换成非阻塞模式
        serverSocketChannel.configureBlocking(false);
        // 3.绑定连接
        serverSocketChannel.bind(new InetSocketAddress(9898));
        // 4.获取选择器
        Selector selector = Selector.open();
        // 5.将通道注册到选择器上，并且指定‘监听接受事件’
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 6.轮询方式获取选择器上已准备就绪的事件
        while (selector.select() > 0) {
            // 7.获取当前选择器上所有注册的事件
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                // 8.获取准备就绪的事件
                SelectionKey next = keyIterator.next();
                // 9.判断是什么条件准备就绪
                if (next.isAcceptable()) {
                    // 10.若‘接收’就绪，获取客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 11.将客户端连接切换成非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 12.将该通道注册到选择器上
                    socketChannel.register(selector, SelectionKey.OP_READ);

                } else if (next.isReadable()) {
                    // 13.获取当前选择器上‘该就绪’状态的通道
                    SocketChannel nextChannel = (SocketChannel) next.channel();

                    // 14.读取数据
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int len = 0;
                    while ((len = nextChannel.read(byteBuffer)) != -1) {
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(), 0, len));
                        byteBuffer.clear();
                    }
                }
                keyIterator.remove();
            }
        }
    }
    /**
     * @description: 客户端
     * @author XLJ
     * @date 2020/10/13 14:03
    */
    //    @Test     用Test注解无法在控制台上进行Scanner输入
    public static void main(String[] args) throws IOException {
        // 1.创建通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        // 2.切换非阻塞模式
        socketChannel.configureBlocking(false);
        // 3.分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 4.发送数据给服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String str = scanner.next();
            byteBuffer.put((new Date().toString() + "\n" + str).getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        // 5.关闭
        socketChannel.close();
    }
}
