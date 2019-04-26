package com.snailmann.redis.cache;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class RedisService {

    @Data
    public static class Number {
        int count = 0;

        public void plus(int num) {
            count += num;
        }

    }


    @Autowired
    private RemoteLock remoteLock;

    public void remoteLockTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Number count = new Number();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {

                //尝试获得分布式锁
                if (remoteLock.tryLock("lock", 30L)) {
                    //如果获得，就 + 1
                    count.plus(1);
                    //执行完就释放锁
                    remoteLock.releaseLock("lock");
                }

                System.out.println(count);
            });
        }


    }


}
