package com.xlj.tools.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品秒杀关注的几个问题：
 * 1. 服务单一职责，独立部署：
 * 秒杀做成独立服务，即使自己抗不住压力，也不影响其他业务
 * 2. 秒杀链接加密：
 * 防止恶意攻击，模拟秒杀请求；防止链接暴露，工作人员提前秒杀商品
 * 3. 库存预热，快速扣减：
 * 秒杀读多写少，提前将库存放入缓存中，用信号量来控制秒杀的请求
 * 4. 动静分离：
 * nginx做好动静分离，保证动态请求才打到后端的服务集群上，使用CDN网络，分担集群的压力
 * 5. 恶意请求拦截：
 * 识别非法攻击请求并拦截，在网关层进行处理
 * 6. 流量错峰：
 * 使用各种手段，将流量分担到最大宽度的时间点（也就是将秒杀操作时间拉长），输入验证码，加入购物车……
 * 7. 限流、熔断、降级
 * 前端限流（时间范围内不能多次点击）+后端限流（限制总量，快速失败返回，不阻塞，熔断隔离防止雪崩）
 * 8. 队列削峰
 * 秒杀的请求进入队列，慢慢去减库存
 *
 * @author xlj
 * @date 2021/5/22
 */
@Slf4j
@RestController
public class SkillProductController {
}
