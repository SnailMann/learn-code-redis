package com.snailmann.demo.redis.stream.message.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author liwenjie
 */
@Slf4j
@Component
public class StreamMessageListener implements StreamListener<String, MapRecord<String, String, String>> {


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        // id
        RecordId messageId = message.getId();

        // k/v
        Map<String, String> body = message.getValue();
        log.info("stream message, messageId={}, stream={}, body={}", messageId, message.getStream(), body);

        // ack
        this.stringRedisTemplate.opsForStream().acknowledge("mgroup", message);
    }
}