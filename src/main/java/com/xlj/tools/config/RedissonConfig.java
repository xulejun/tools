package com.xlj.tools.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁redisson配置
 *
 * @author xlj
 * @date 2021/5/20
 */
@Configuration
public class RedissonConfig {

    /**
     * 所有对redisson的使用都是通过redissonClient对象
     *
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        // 创建一个配置
        Config config = new Config();
        // 可以用"rediss://"来启用SSL连接
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");

//         集群模式，最低要求是必须有三个主节点
//        config.useClusterServers()
//                // 集群状态扫描间隔时间，单位是毫秒
//                .setScanInterval(2000)
//                .addNodeAddress("redis://127.0.0.1:7000", "redis://127.0.0.1:7001")
//                .addNodeAddress("redis://127.0.0.1:7002");

        // 创建出RedissonClient实例
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
