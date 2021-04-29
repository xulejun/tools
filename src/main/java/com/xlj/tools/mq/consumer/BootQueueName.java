package com.xlj.tools.mq.consumer;

/**
 * 队列名
 *
 * @author xlj
 * @date 2020/12/1 20:55
 */
public class BootQueueName {
    public static final String BOOT_SIMPLE_QUEUE_NAME = "boot_simple_queue";
    public static final String BOOT_WORK_QUEUE_NAME = "boot_work_queue";
    public static final String BOOT_FANOUT_EXCHANGE_NAME = "boot_fanout_exchange";
    public static final String BOOT_DIRECT_EXCHANGE_NAME = "boot_direct_exchange";
    public static final String BOOT_TOPIC_EXCHANGE_NAME = "boot_topic_exchange";
    public static final String BOOT_INFO_ROUTING_KEY = "info";
    public static final String BOOT_ERROR_ROUTING_KEY = "error";
    public static final String BOOT_USER_ROUTING_KEY = "user.save";
    public static final String BOOT_ORDER_ROUTING_KEY = "order.pay.money";
}
