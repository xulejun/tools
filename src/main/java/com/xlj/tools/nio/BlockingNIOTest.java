package com.xlj.tools.nio;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 阻塞型NIO
 *
 * @author XLJ
 * @date 2020/10/12
 */
public class BlockingNIOTest {
    /**
     * @description: 客户端
     * @author XLJ
     * @date 2020/10/13 10:03
    */
    @Test
    public void client() throws Exception {
        // 1.创建客户端Socket通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        // 2.创建文件通道
        FileChannel fileChannel = FileChannel.open(Paths.get("C:\\Users\\DELL\\Desktop\\新建文本文档.txt"), StandardOpenOption.READ);
        // 3.分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 4.读取文件
        while (fileChannel.read(byteBuffer) != -1){
            byteBuffer.flip();  // 切换成读的模式
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        socketChannel.shutdownOutput();

        // 阻塞型NIO无法接受服务端的反馈
        int len = 0;
        while ((len = socketChannel.read(byteBuffer)) != -1) {
            byteBuffer.flip();
            System.out.println(new String(byteBuffer.array(), 0, len));
            byteBuffer.clear();
        }

        // 5.关闭通道
        socketChannel.close();
        fileChannel.close();
    }

    /**
     * @description: 服务端
     * @author XLJ
     * @date 2020/10/13 10:04
    */
    @Test
    public void server() throws Exception {
        // 1.创建服务端Socket通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 2.绑定端口号
        ServerSocketChannel bind = serverSocketChannel.bind(new InetSocketAddress(9898));
        // 3. 与客户端建立连接
        SocketChannel socketChannel = bind.accept();

        // 4.创建文件通道
        FileChannel fileChannel = FileChannel.open(Paths.get("C:\\Users\\DELL\\Desktop\\新建文本文档1.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        // 5.创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 6.数据传输：客户端通道读取，服务端通道写入保存
        while (socketChannel.read(byteBuffer) != -1){
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        // 反馈给客户端
        byteBuffer.put("来自服务端的反馈".getBytes());
        byteBuffer.flip();
        fileChannel.write(byteBuffer);

        // 7.关闭资源
        serverSocketChannel.close();
        fileChannel.close();
        socketChannel.close();
    }

}
