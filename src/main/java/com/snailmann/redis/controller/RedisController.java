package com.snailmann.redis.controller;

import com.snailmann.redis.service.RedisSentinelService;
import com.snailmann.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RedisController {

    @Autowired
    RedisService redisService;
    @Autowired
    RedisSentinelService redisSentinelService;

    @GetMapping("redis/test")
    public void test(){
        redisService.hsetTest();
        redisService.hmsetTest();
        redisService.pipelineTest();
    }

    @GetMapping("redis/sentinel/test")
    public void sentinelTest(){
       redisSentinelService.sentinelTest();
    }

}
