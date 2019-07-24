package com.snailmann.redis.queue.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisPubliser {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * Redis PubSub 生产者
     *
     * @return
     */
    public void publish() {
        redisTemplate.convertAndSend("java.news", "hello java?");
        redisTemplate.convertAndSend("java.news", "let's learn java everyday!!");
        redisTemplate.convertAndSend("java.news", "easy java 3.7");
    }


}
