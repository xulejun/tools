package com.xlj.tools.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 日志切面
 *
 * @author xlj
 * @date 2021/4/29
 */
@Aspect     // 1.表明这是一个切面类
@Component
@Slf4j
public class MyLogAspect {
    /**
     * 2. PointCut表示这是一个切点，@annotation表示这个切点切到一个注解上，后面带该注解的全类名
     * 切面最主要的就是切点，所有的故事都围绕切点发生
     * logPointCut()代表切点名称
     */
    @Pointcut("@annotation(com.xlj.tools.annotation.MyLog)")
    public void logPointCut() {
    }

    ;

    /**
     * 3. 环绕通知
     */
    @Around("logPointCut()")
    public void logAround(ProceedingJoinPoint joinPoint) {
        // 获取方法名称
        String methodName = joinPoint.getSignature().getName();
        // 获取入参
        Object[] param = joinPoint.getArgs();

        StringBuilder sb = new StringBuilder();
        for (Object o : param) {
            sb.append(o + "; ");
        }
        log.info("进入 {} 方法，参数为：{}", methodName, sb.toString());

        // 继续执行方法
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        log.info("{} 方法执行结束", methodName);
    }
}
