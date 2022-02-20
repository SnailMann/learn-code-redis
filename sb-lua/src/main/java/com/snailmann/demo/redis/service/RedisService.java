package com.snailmann.demo.redis.service;

import com.snailmann.demo.redis.annotation.Rate;
import com.snailmann.demo.redis.storage.RemoteLock;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

@Component
public class RedisService {

    @Autowired
    RemoteLock remoteLock;

    /**
     * 测试类
     */
    @Data
    public static class Counter {

        private Map<String, Integer> counter = new ConcurrentHashMap<>(100);

        public void incr(String name) {
            counter.compute(name, (k, v) ->
                    Objects.isNull(v) ? 1 : v + 1);
        }
    }

    /**
     * 分布式锁的测试
     * 1. 使用 lua 脚本来获取锁，组合了 setnx 和 expire 命令
     * 2. 使用 lua 脚本来释放锁，组合了 get 和 del 命令
     */
    @SneakyThrows
    public void remoteLockTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        Counter counter = new Counter();
        for (int i = 0; i < 10; i++) {
            CompletableFuture.runAsync(() -> {
                //锁全局唯一标识，val
                String val = System.currentTimeMillis() + "";
                //尝试获得分布式锁
                if (remoteLock.lock("lock", val, 30L)) {
                    //如果获得，就 + 1
                    counter.incr(Thread.currentThread().getName());
                    //执行完就释放锁
                    remoteLock.release("lock", val);
                }
            }, executorService);
        }
        TimeUnit.SECONDS.sleep(1);
        System.out.println(counter.getCounter());
    }

    /**
     * 限流测试，模拟接口
     * 60s内请求了5次，就开始限流
     */
    @Rate(key = "controller", threshold = 10, window = 1)
    public void ratelimitTest() {
        System.out.println("请求...");
    }

}
