package com.snailmann.redis.queue.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 1. 先模拟push
 * 2. 再模拟pop的阻塞队列操作
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisNativeMsgQueueServiceTest {

    @Autowired
    RedisNativeMsgQueueService redisNativeMsgQueueService;

    /**
     *
     */
    @Test
    public void lpush() {
        redisNativeMsgQueueService.lpush();
    }

    @Test
    public void rpop() {
        redisNativeMsgQueueService.rpop();
    }
}