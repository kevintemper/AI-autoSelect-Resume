package com.example.zwtcampuscareerview.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AopLogger {

    private static final Logger logger = LoggerFactory.getLogger(AopLogger.class);

    // 在所有控制器方法执行之前记录请求信息
    @Before("execution(* com.example.zwtcampuscareerview.controllers.*.*(..))")
    public void logBeforeControllerMethods(JoinPoint joinPoint) {
        logger.info("Controller: {}", joinPoint.getSignature().getDeclaringTypeName());
        logger.info("Method: {}", joinPoint.getSignature().getName());
        logger.info("Arguments: ");
        for (Object arg : joinPoint.getArgs()) {
            logger.info(" - {}", arg);
        }
    }

    // 在服务层方法成功返回时记录返回值
    @AfterReturning(pointcut = "execution(* com.example.zwtcampuscareerview.services.*.*(..))", returning = "result")
    public void logAfterServiceMethods(JoinPoint joinPoint, Object result) {
        logger.info("Service: {}", joinPoint.getSignature().getDeclaringTypeName());
        logger.info("Method: {}", joinPoint.getSignature().getName());
        logger.info("Result: {}", result);
    }
}
