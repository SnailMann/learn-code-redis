package com.snailmann.redis.service;

import com.snailmann.redis.annotation.RateLimit;
import com.snailmann.redis.cache.RemoteLock;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class RedisService {

    @Autowired
    RemoteLock remoteLock;

    /**
     * 测试类
     */
    @Data
    public static class Number {
        int count = 0;

        public void plus(int num) {
            count += num;
        }

    }

    /**
     * 分布式锁的测试
     * 1. 使用lua脚本来获取锁，组合了setnx和expire命令
     * 2. 使用lua脚本来释放锁，组合了get和del命令
     */
    public void remoteLockTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Number count = new Number();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {

                //锁全局唯一标识，val
                String val = System.currentTimeMillis() + "";
                //尝试获得分布式锁
                if (remoteLock.tryLock("lock", val, 30L)) {
                    //如果获得，就 + 1
                    count.plus(1);
                    //执行完就释放锁
                    remoteLock.releaseLock("lock", val);
                }

                System.out.println(count);
            });
        }
    }

    /**
     * 限流测试，模拟接口
     * 60s内请求了5次，就开始限流
     */
    @RateLimit(key = "controller", limitNum = 5, ttl = 60)
    public void ratelimitTest() {
        System.out.println("请求...");
    }

}
