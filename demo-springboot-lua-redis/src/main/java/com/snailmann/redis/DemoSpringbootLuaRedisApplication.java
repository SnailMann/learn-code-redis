package com.snailmann.redis;

import com.snailmann.redis.cache.RedisService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoSpringbootLuaRedisApplication {

    public static void main(String[] args) {
        ApplicationContext context =
                new SpringApplicationBuilder(DemoSpringbootLuaRedisApplication.class).web(WebApplicationType.NONE).run(args);
        RedisService redisService = context.getBean(RedisService.class);
        redisService.remoteLockTest();
    }

}
