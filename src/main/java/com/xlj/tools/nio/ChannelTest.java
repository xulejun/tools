package com.xlj.tools.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * @author xlj
 * @date 2020/10/11 16:18
 */

public class ChannelTest {
    /**
     * @description 利用通道完成对文件的复制
     * @author xlj
     * @date 2020/10/11 16:35
     */
    @Test
    public void test1() throws Exception {
        long start = System.currentTimeMillis();

        // 1.创建文件路径（当前目录的根路径）
        FileInputStream fileInputStream = new FileInputStream("1.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("2.jpg");

        // 2.获取通道
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        // 3.分配指定大小的非直接缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 4.将通道的数据放入缓冲区
        while (inputStreamChannel.read(byteBuffer) != -1) {
            // 切换成读模式
            byteBuffer.flip();
            // 写入数据
            outputStreamChannel.write(byteBuffer);
            // 清空缓冲区
            byteBuffer.clear();
        }

        // 5.关闭
        fileInputStream.close();
        fileOutputStream.close();
        inputStreamChannel.close();
        outputStreamChannel.close();

        long end = System.currentTimeMillis();
        System.out.println("test1耗费时间：" + (end - start));   // 24

    }

    /**
     * @description 使用直接缓冲区实现对文件的复制（内存映射文件）
     * @author xlj
     * @date 2020/10/11 16:42
     */
    @Test
    public void test2() throws Exception {
        long start = System.currentTimeMillis();

        FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);

        // 内存映射文件
        MappedByteBuffer inMapBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMapBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        // 直接对缓冲区进行读写
        byte[] bytes = new byte[inMapBuffer.limit()];
        inMapBuffer.get(bytes);
        outMapBuffer.put(bytes);

        // 关闭
        inChannel.close();
        outChannel.close();

        long end = System.currentTimeMillis();
        System.out.println("test1耗费时间：" + (end - start));
    }

    /**
     * @description 通道之间的数据传输
     * @author xlj
     * @date 2020/10/11 17:06
     */
    @Test
    public void test3() throws Exception {
        FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("4.jpg"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);

        // 数据之间的传输
        inChannel.transferTo(0, inChannel.size(), outChannel);
//        outChannel.transferFrom(inChannel,0,inChannel.size());

        // 关闭
        inChannel.close();
        outChannel.close();
    }

    /**
     * @description: 分散和聚集
     * @author XLJ
     * @date 2020/10/12 17:31
     */
    @Test
    public void test4() throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("C:\\Users\\DELL\\Desktop\\新建文本文档.txt", "rw");

        // 1.获取通道
        FileChannel fileChannel = randomAccessFile.getChannel();
        // 2.分配指定的大小
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(10);
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(1024);
        // 3.分散读取
        ByteBuffer[] byteBuffers = {byteBuffer1,byteBuffer2};
        fileChannel.read(byteBuffers);
        for (ByteBuffer byteBuffer : byteBuffers) {
            byteBuffer.flip();
        }
        // 输出容量100的数据
        System.out.println(new String(byteBuffers[0].array(),0,byteBuffers[0].limit()));
        // 输出容量1024的数据
        System.out.println(new String(byteBuffers[1].array(),0,byteBuffers[1].limit()));

        // 聚集写入
        RandomAccessFile randomAccessFile1 = new RandomAccessFile("C:\\Users\\DELL\\Desktop\\新建文本文档1.txt", "rw");
        FileChannel file1Channel = randomAccessFile1.getChannel();

        file1Channel.write(byteBuffers);
    }

    /**
     * @description: 字符编码格式
     * @author XLJ
     * @date 2020/10/12 18:30
    */
    @Test
    public void test5(){
        SortedMap<String, Charset> charsetSortedMap = Charset.availableCharsets();
        Set<Map.Entry<String, Charset>> entries = charsetSortedMap.entrySet();
        // 所有可操作的字符编码格式
        for (Map.Entry<String, Charset> entry : entries) {
            System.out.println(entry.getKey()+"="+entry.getValue());
        }
    }

    /**
     * @description: 操作字符集
     * @author XLJ
     * @date 2020/10/12 19:04
    */
    @Test
    public void test6() throws Exception {
        Charset gbk = Charset.forName("GBK");
        // 编码器
        CharsetEncoder encode = gbk.newEncoder();
        // 解码器
        CharsetDecoder decoder = gbk.newDecoder();

        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("xlj");
        charBuffer.flip();

        // 编码——字符串转字符
        ByteBuffer byteBuffer = encode.encode(charBuffer);
        for (int i = 0; i <byteBuffer.limit() ; i++) {
            System.out.println(byteBuffer.get());
        }
        // 解码——字符转字符串
        byteBuffer.flip();
        CharBuffer charBuffer1 = decoder.decode(byteBuffer);
        System.out.println(charBuffer1);

        // 样例：利用UTF-8进行解码，出现乱码
        System.out.println("---------------------------");
        Charset utf = Charset.forName("UTF-8");
        byteBuffer.flip();
        CharBuffer charBuffer2 = utf.decode(byteBuffer);
        System.out.println(charBuffer2);
    }
}
