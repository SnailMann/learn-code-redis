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

/**
 * 限流缓存
 */
@Component
public class RateLimitCache {

    private final String KEY = "ratelimit:";
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    /**
     * 是否限流
     * true限流，false不限流
     *
     * @return
     */
    public boolean isRateLimit(String key, int limitNum, long ttl) {
        RedisScript<Long> script = new DefaultRedisScript<>(ResourceUtils.getLua("classpath:lua/ratelimit.lua"),Long.class);
        List<String> keys = Stream.of(KEY + key).collect(Collectors.toList());
        //1 : 不限流 0 ： 限流
        Long result = redisTemplate.execute(script, keys, limitNum + "", ttl + "");
        //result如果是0，则返回true, 如果是1，则返回false
        return result != null && result.equals(0L);
    }


}
