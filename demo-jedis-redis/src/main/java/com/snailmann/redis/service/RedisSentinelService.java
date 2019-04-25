package com.snailmann.redis.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisSentinelService {

    @Autowired
    JedisSentinelPool jedisSentinelPool;

    public void sentinelTest() {

        Jedis jedis = null;


        int counter = 0;
        while (true) {
            counter++;
            try {
                jedis = jedisSentinelPool.getResource();
                int index = new Random().nextInt(10000);
                String key = "k-" + index;
                String value = "v-" + index;
                counter++;
                jedis.set(key, value);
                log.info("{} value is {} ,counter {}", key, jedis.get(key), counter);
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (Exception e) {
                e.getStackTrace();
                log.error(e.getMessage(), e);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
    }

}
