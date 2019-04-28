package com.snailmann.redis.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.Target;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceTest {

    @Autowired
    RedisService redisService;

    /**
     * 分布式锁测试
     */
    @Test
    public void remoteLockTest() {
        redisService.remoteLockTest();
    }

    /**
     * 模拟接口请求，访问接口10次
     */
    @Test
    public void ratelimitTest() {
        for (int i = 0; i < 10; i++) {
            redisService.ratelimitTest();
        }

    }
}