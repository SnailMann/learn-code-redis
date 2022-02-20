package com.snailmann.demo.redis.stream.message.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liwenjie
 */
@Slf4j
@Component
public class StreamConsumer {

    private final StringRedisTemplate stringRedisTemplate;

    public StreamConsumer(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void consumer(String group, String key) {
        stringRedisTemplate.opsForStream().createGroup(key, group);
        List<MapRecord<String, String, String>> msgs = stringRedisTemplate.<String, String>opsForStream()
                .read(Consumer.from(group, key), StreamOffset.create(key, ReadOffset.lastConsumed()));
        msgs.forEach(entries -> System.out.println(entries.getId() + ":" + entries.getValue()));
    }

}
