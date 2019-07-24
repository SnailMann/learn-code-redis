package com.snailmann.redis.queue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RedisNativeMsgQueueService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final String KEY = "test:list";

    /**
     * 模拟消息队列入队操作，5批入队，每批入队5个消息
     */
    public void lpush() {

        List<Integer> nums = Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList());
        for (int i = 0; i < 5; i++) {
            redisTemplate.opsForList().leftPushAll(KEY, nums.toArray());
            System.out.println("rpush :" + nums);
            //每隔5秒执行一次
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 模拟阻塞队列的弹出操作，当没有消息时，将线程阻塞
     * 1. 但是Spring Data Redis, 并没有完全的阻塞api ,可是给了一个超时时间。如果超时，会返回null
     */
    public void rpop() {
        while (true) {
            Integer result = (Integer) redisTemplate.opsForList().rightPop(KEY, 5, TimeUnit.SECONDS);
            if (null == result) {
                continue;
            }
            System.out.println(result);
        }
    }

}
