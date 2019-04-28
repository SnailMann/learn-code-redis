package com.snailmann.redis;

import com.snailmann.redis.service.RedisService;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoJedisLuaRedisApplication {

    public static void main(String[] args) {
        //非web方式启动
        ApplicationContext context = new SpringApplicationBuilder(DemoJedisLuaRedisApplication.class).web(WebApplicationType.NONE).run(args);
        RedisService redisService = context.getBean(RedisService.class);
        redisService.remoteLockTest();
    }

}
