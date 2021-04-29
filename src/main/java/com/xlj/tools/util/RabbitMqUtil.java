package com.xlj.tools.util;

import cn.hutool.core.net.NetUtil;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * rabbitmq工具类
 *
 * @author xlj
 * @date 2020/11/28 16:47
 */
public class RabbitMqUtil {
    /**
     * rabbitmq端口号
     */
    private static final int RABBITMQ_PORT = 15672;

    public static ConnectionFactory connectionFactory;

    // 重量级资源，类加载只执行一次
    static {
        connectionFactory = new ConnectionFactory();
        // 设置连接RabbitMQ地址
        connectionFactory.setHost("localhost");
        // 设置虚拟主机-默认可不设置
        connectionFactory.setVirtualHost("/");
        // 设置用户名-默认可不设置
        connectionFactory.setUsername("guest");
        // 设置密码-默认可不设置
        connectionFactory.setPassword("guest");
    }

    /**
     * @description 连接工厂配置
     * @author xlj
     * @date 2020/11/28 16:44
     */
    public static Connection connectFactory() {
        // 判断RabbitMQ服务是否启动
        if (NetUtil.isUsableLocalPort(RABBITMQ_PORT)) {
            System.out.println("Rabbit服务未启动");
            System.exit(1);
        }

        try {
            return connectionFactory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
