package com.xlj.tools.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * jedis 实现分布式锁
 *
 * @author lejunxu
 */
public class RedisLockUtil {
    private static Logger logger = LoggerFactory.getLogger(RedisLockUtil.class);

    private static final Long RELEASE_SUCCESS = 1L;
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 尝试等待或者获取分布式锁
     *
     * @param jedis
     * @param lockKey
     * @param requestId 锁value（唯一值，可为uuid）
     * @param ttl       锁过期时间（毫秒）
     * @param timeout   超时时间（毫秒）
     * @return
     */
    public static boolean tryGetAndWaitLock(Jedis jedis, String lockKey, String requestId, int ttl, long timeout) {
        long time = System.currentTimeMillis() + timeout;
        boolean isGetLock = tryGetDistributedLock(jedis, lockKey, requestId, ttl);
        // 若没有获取到锁，在超时时间内持续获取
        while (System.currentTimeMillis() < time && !isGetLock) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                logger.warn("尝试重新获取异常：", e);
            }
            isGetLock = tryGetDistributedLock(jedis, lockKey, requestId, ttl);
        }
        return isGetLock;
    }

    /**
     * 尝试获取分布式锁
     *
     * @param jedis      Redis client
     * @param lockKey    lock
     * @param requestId  request ID
     * @param expireTime overtime
     * @return whether to succeed
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 释放分布式锁
     *
     * @param jedis     Redis client
     * @param lockKey   lock
     * @param requestId request ID
     * @return whether the release is successful
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        String script =
                "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

}
