package com.snailmann.demo.redis;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author liwenjie
 */
@SpringBootApplication
public class DemoSpringbootLuaRedisApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DemoSpringbootLuaRedisApplication.class).web(WebApplicationType.NONE).run(args);
    }


}
