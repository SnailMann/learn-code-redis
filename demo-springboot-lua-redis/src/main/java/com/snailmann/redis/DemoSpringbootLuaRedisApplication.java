package com.snailmann.redis;

import com.snailmann.redis.service.RedisService;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class DemoSpringbootLuaRedisApplication {

    public static void main(String[] args) {
        ApplicationContext context =
                new SpringApplicationBuilder(DemoSpringbootLuaRedisApplication.class).web(WebApplicationType.NONE).run(args);

    }




}
