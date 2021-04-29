package com.xlj.tools.basis.nio;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * @author XLJ
 * @date 2020/10/13
 */
public class PipeTest {
    @Test
    public void single() throws IOException {
        // 创建Pipe通道
        Pipe pipe = Pipe.open();
        Pipe.SinkChannel sink = pipe.sink();

        // 将数据存到缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("这是Pipe单项通道测试".getBytes());
        byteBuffer.flip();
        // 写入数据
        sink.write(byteBuffer);

        // 读取数据
        Pipe.SourceChannel source = pipe.source();
        byteBuffer.flip();
        System.out.println(new String(byteBuffer.array(),0,source.read(byteBuffer)));

        source.close();
        sink.close();
    }
}
