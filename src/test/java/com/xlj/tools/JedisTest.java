package com.xlj.tools;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * jedis连接Redis哨兵模式
 *
 * @author xulejun
 * @date 2021/9/23
 */
public class JedisTest {

    @Test
    public void testSentinel() {
        String masterName = "mymaster";
        String password = "123456";
        // 设置参数
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(10);
        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(5);
        // 最小空闲连接数, 默认0
        jedisPoolConfig.setMinIdle(5);
        // 哨兵信息，注意填写哨兵的地址
        Set<String> sentinels = new HashSet<>();
        sentinels.add("127.0.0.1:6390");
        sentinels.add("127.0.0.1:6391");
        // 创建连接池，当master宕机后，哨兵模式自动切换到slave上
//        JedisSentinelPool pool = new JedisSentinelPool(masterName, sentinels, jedisPoolConfig, password);
//        if ("6381".equals(String.valueOf(pool.getCurrentHostMaster().getPort()))) {
//            pool = new JedisSentinelPool(masterName, sentinels, jedisPoolConfig);
//        }
        JedisPool pool = new JedisPool("10.128.188.44", 6379);
        // 获取客户端
        try (Jedis jedis = pool.getResource()) {
            jedis.set("username", "legendxu");
            String value = jedis.get("username");
            System.out.println(value);
        } catch (Exception e) {
            System.out.println("jedis异常：" + e);
        }
    }
}
