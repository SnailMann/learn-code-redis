package com.snailmann.redis.annotation;

import java.lang.annotation.*;

/**
 * 限流注解
 * 被该注解修饰的方法，会启动限流策略
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流的Redis Key
     *
     * @return
     */
    String key() default "";

    /**
     * 达多少次数后限流
     *
     * @return
     */
    int limitNum() default 5;


    /**
     * ttl秒内，达limitNum次数后启动限流
     *
     * @return
     */
    long ttl() default 60;


}
