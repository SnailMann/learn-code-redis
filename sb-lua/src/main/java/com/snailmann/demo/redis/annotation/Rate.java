package com.snailmann.demo.redis.annotation;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author liwenjie
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Rate {

    /**
     * key
     */
    String key() default "";

    /**
     * 阀值
     */
    int threshold() default 5;

    /**
     * 时间窗口, s
     */
    long window() default 60;


}
