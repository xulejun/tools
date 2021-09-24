package com.xlj.tools;

import org.junit.Test;
import redis.clients.jedis.Jedis;
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
        JedisSentinelPool pool = new JedisSentinelPool(masterName, sentinels, jedisPoolConfig, password);
        if ("6381".equals(String.valueOf(pool.getCurrentHostMaster().getPort()))) {
            pool = new JedisSentinelPool(masterName, sentinels, jedisPoolConfig);
        }
        // 获取客户端
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) {
                pool.close();
            }
        }
        // 执行命令，增、查
        jedis.set("id", "1009");
        String value = jedis.get("id");
        System.out.println(value);
    }
}
