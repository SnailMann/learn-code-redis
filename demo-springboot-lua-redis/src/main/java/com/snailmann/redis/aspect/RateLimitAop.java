package com.snailmann.redis.aspect;

import com.snailmann.redis.annotation.RateLimit;
import com.snailmann.redis.cache.RateLimitCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/***
 * 限流切面
 */
@Slf4j
@Aspect
@Component
public class RateLimitAop {


    @Autowired
    RateLimitCache rateLimitCache;

    @Around("@annotation(com.snailmann.redis.annotation.RateLimit)")
    public Object pass(ProceedingJoinPoint point) {

        //获得类，方法名，参数
        Class<?> clazz = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        Class[] args = ((MethodSignature) point.getSignature()).getParameterTypes();
        //限流参数
        int limitNum = 5;
        long ttl = 60L;
        String key = "";

        try {
            Method method = clazz.getMethod(methodName, args);
            if (method.isAnnotationPresent(RateLimit.class)) {
                RateLimit annotation = method.getAnnotation(RateLimit.class);
                limitNum = annotation.limitNum();
                ttl = annotation.ttl();
                key = annotation.key();
            }

            boolean result = rateLimitCache.isRateLimit(key, limitNum, ttl);

            if (result) {
                throw new RuntimeException("限流中..." + ttl + "s内，已达" + limitNum + "次");
            }
        } catch (NoSuchMethodException e) {
            log.error("限流错误", e);
        }

        return null;
    }

}
