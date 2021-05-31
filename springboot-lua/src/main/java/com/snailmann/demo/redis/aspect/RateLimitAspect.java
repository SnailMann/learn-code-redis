package com.snailmann.demo.redis.aspect;

import com.snailmann.demo.redis.annotation.Rate;
import com.snailmann.demo.redis.storage.RateLimit;
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
 * @author liwenjie
 */
@Slf4j
@Aspect
@Component
public class RateLimitAspect {


    @Autowired
    RateLimit rateLimit;

    @Around("@annotation(com.snailmann.demo.redis.annotation.Rate)")
    public Object pass(ProceedingJoinPoint point) {

        //获得类，方法名，参数
        Class<?> clazz = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        Class[] args = ((MethodSignature) point.getSignature()).getParameterTypes();
        //限流参数
        int threshold = 5;
        long window = 60L;
        String key = "";

        try {
            Method method = clazz.getMethod(methodName, args);
            if (method.isAnnotationPresent(Rate.class)) {
                Rate annotation = method.getAnnotation(Rate.class);
                threshold = annotation.threshold();
                window = annotation.window();
                key = annotation.key();
            }

            boolean result = rateLimit.rate(key, threshold, window);

            if (result) {
                throw new RuntimeException("限流中..." + window + "s内，已达" + threshold + "次");
            }
        } catch (NoSuchMethodException e) {
            log.error("限流错误", e);
        }

        return null;
    }

}
