package com.snailmann.redis.cache;

import com.snailmann.redis.util.ResourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RemoteLock {

    private final String KEY = "";

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    /**
     * 加锁
     *
     * @param key
     * @param val
     * @param ttl
     * @return
     */
    public boolean tryLock(String key, String val, long ttl) {
        List<String> keys = Stream.of(KEY + key).collect(Collectors.toList());
        RedisScript<Long> SETNX_EXPIRE_SCRIPT = new DefaultRedisScript<>(ResourceUtils.getLua("classpath:lua/trylock.lua"), Long.class);

        while (true) {
            Long result = redisTemplate.execute(SETNX_EXPIRE_SCRIPT, keys, val, ttl + "");
            if (result != null && result.equals(1L)) {
                return true;
            }
        }
    }


    /**
     * 释放锁
     *
     * @param key
     * @param val
     * @return
     */
    public boolean releaseLock(String key, String val) {
        List<String> keys = Stream.of(KEY + key).collect(Collectors.toList());
        RedisScript<Long> GET_DEL_SCRIPT = new DefaultRedisScript<>(ResourceUtils.getLua("classpath:lua/releaseLock.lua"), Long.class);
        Long result = redisTemplate.execute(GET_DEL_SCRIPT, keys, val);
        return result != null && result > 0;
    }


}
