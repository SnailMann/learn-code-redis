package com.snailmann.demo.redis.storage;

import com.snailmann.demo.redis.util.ResourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 限流缓存
 *
 * @author liwenjie
 */
@Component
public class RateLimit {

    private static final String KEY = "ratelimit:";

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    private final DefaultRedisScript<Long> script;

    public RateLimit() {
        this.script = new DefaultRedisScript<>();
        script.setLocation(ResourceUtils.resource("lua/ratelimit.lua"));
        script.setResultType(Long.class);
    }

    /**
     * 是否限流
     */
    public boolean rate(String key, int threshold, long window) {
        List<String> keys = Stream.of(KEY + key).collect(Collectors.toList());
        //1 : 不限流 0 ： 限流
        Long result = redisTemplate.execute(script, keys, threshold + "", window + "");
        return result != null && result.equals(0L);
    }


}
