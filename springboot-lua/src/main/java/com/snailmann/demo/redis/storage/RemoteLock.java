package com.snailmann.demo.redis.storage;

import com.snailmann.demo.redis.util.ResourceUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liwenjie
 */
@Component
public class RemoteLock {

    private final String KEY = "";

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    private final RedisScript<Long> lockScript;
    private final RedisScript<Long> releaseScript;

    public RemoteLock() {
        this.releaseScript = new DefaultRedisScript<>(ResourceUtils.getLua("classpath:lua/releaseLock.lua"), Long.class);
        this.lockScript = new DefaultRedisScript<>(ResourceUtils.getLua("classpath:lua/trylock.lua"), Long.class);
    }


    /**
     * lock
     */
    @SneakyThrows
    public boolean lock(String key, String val, long ttl) {
        List<String> keys = Stream.of(KEY + key).collect(Collectors.toList());
        while (true) {
            Long result = redisTemplate.execute(lockScript, keys, val, ttl + "");
            if (result != null && result.equals(1L)) {
                return true;
            }
            TimeUnit.MILLISECONDS.sleep(10);
        }
    }


    /**
     * release
     */
    public boolean release(String key, String val) {
        List<String> keys = Stream.of(KEY + key).collect(Collectors.toList());
        Long result = redisTemplate.execute(releaseScript, keys, val);
        return result != null && result > 0;
    }


}
