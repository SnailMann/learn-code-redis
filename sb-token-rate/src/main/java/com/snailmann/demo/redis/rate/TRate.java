package com.snailmann.demo.redis.rate;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 令牌桶测试
 * 后期改 lua 脚本，保证原子性
 * @author liwenjie
 */
@Slf4j
@Component
public class TRate implements Rate {

    double rate = 0.6;

    int tokens = 0;

    int bucket = 100;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public  synchronized boolean getToken(String key) {

        long now = getNowTime();
        String lastUpdateTime = redisTemplate.opsForValue().get(key);
        long time = now;
        if (!StringUtils.isBlank(lastUpdateTime)) {
            time = Long.parseLong(lastUpdateTime);
        }
        tokens = (int) (tokens + (now - time) * rate);
        tokens = Math.min(tokens, bucket);
        if (tokens < 1) {
            System.out.println("[False]" + Thread.currentThread().getName() + " - 当前令牌数量：" + tokens);

            return false;
        }
        System.out.println("[True]" + Thread.currentThread().getName() + " - 当前令牌数量：" + tokens);
        tokens--;
        redisTemplate.opsForValue().set(key, now + "");

        return true;
    }

    private long getNowTime() {
        return System.currentTimeMillis();
    }


}
