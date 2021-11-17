package com.xlj.tools.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

/**
 * 通过aop 实现接口调用校验
 * 使用打开下面两个注解
 *
 * @author legend xu
 */
@Component
@Aspect
public class ControllerIntercept {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerIntercept.class);

    @Autowired
    private HttpServletRequest request;

    //    @Pointcut("execution(* com.xlj.tools.controller.*.*(..)) ")
    public void pointcut() {
    }

    //    @Around("pointcut()")
    public Object intercept(ProceedingJoinPoint pjp) throws Throwable {
        String apiType = "";
        String userName = "NA";
        int code = HttpStatus.OK.value();
        String message = HttpStatus.OK.getReasonPhrase();
        try {
            apiType = pjp.getSignature().getName();
            // obj为"anonymousUser"时的权限校验
            String auth = request.getHeader("Authorization");
            if (auth == null || !auth.startsWith("Basic ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Auth Failure");
            }
            String authValue = new String(Base64.getDecoder().decode(auth.substring(6)));
            boolean isLigalUser = authValue.equals("自定义的鉴权码");
            if (!isLigalUser) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Auth Failure");
            }
            // 调用真实业务方法
            return pjp.proceed();
        } catch (Exception e) {
            LOGGER.error("process task error:{}, username:{}, apiType:{}", code, userName, apiType, e);
            // 参数null 可用通用response对象封装
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

}
