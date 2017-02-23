package com.mail.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by zengping on 2016/11/11.
 * 对账信息用户权限拦截
 */
@Aspect
@Component
@EnableAspectJAutoProxy
public class RequestAspect {
    private  static final Logger LOGGER = LoggerFactory.getLogger(RequestAspect.class);

    @Around("execution(* com.mail.controller.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        LOGGER.info(new StringBuilder("Controller执行方法 : ").append(methodSignature.getDeclaringTypeName())
                .append(".").append(joinPoint.getSignature().getName()).append("(")
                .append(Arrays.toString(joinPoint.getArgs())).append(")").toString());
        return joinPoint.proceed(joinPoint.getArgs());
    }
}
