package com.snailmann.redis.queue.pubsub;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisPubliserTest {

    @Autowired
    RedisPubliser redisPubliser;

    @Test
    public void publish() {
        redisPubliser.publish();
    }
}