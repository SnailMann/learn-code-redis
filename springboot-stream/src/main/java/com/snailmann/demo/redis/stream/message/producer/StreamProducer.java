package com.snailmann.demo.redis.stream.message.producer;

import com.snailmann.demo.redis.stream.message.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liwenjie
 */
@Slf4j
@Component
public class StreamProducer {

    private final StringRedisTemplate stringRedisTemplate;

    public StreamProducer(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void producer(String key, Event event) {
        Map<String, String> map = new HashMap<>(2);
        BeanMap beanMap = BeanMap.create(event);
        beanMap.keySet().forEach(o -> map.put(o.toString(), beanMap.get(o).toString()));
        StringRecord record = StreamRecords.string(map).withStreamKey(key);
        stringRedisTemplate.opsForStream().add(record);
        System.out.println(record);
    }

}
