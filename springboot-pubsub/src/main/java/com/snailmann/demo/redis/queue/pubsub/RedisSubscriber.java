package com.snailmann.demo.redis.queue.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class RedisSubscriber extends MessageListenerAdapter {


    @Override
    public void onMessage(Message message, byte[] bytes) {
        /*System.out.println(message);*/

        System.out.println("监听到生产者发送的消息: " + message);
    }

}
