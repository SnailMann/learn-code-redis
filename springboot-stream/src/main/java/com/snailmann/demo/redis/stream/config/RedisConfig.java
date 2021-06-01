package com.snailmann.demo.redis.stream.config;

import com.snailmann.demo.redis.stream.message.consumer.StreamMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamReceiver;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * @author liwenjie
 */
@Configuration
public class RedisConfig {

    /**
     * method one
     */
    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(StreamMessageListener streamMessageListener, RedisConnectionFactory redisConnectionFactory) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> containerOptions =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder().pollTimeout(Duration.ofMillis(100)).build();
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(redisConnectionFactory,
                containerOptions);
        container.receive(StreamOffset.fromStart("mstream"), streamMessageListener);
        container.start();
        return container;
    }

    /**
     * method two
     */
    @Bean
    public StreamReceiver<String, MapRecord<String, String, String>> streamReceiver(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        StreamReceiver.StreamReceiverOptions<String, MapRecord<String, String, String>> options = StreamReceiver.StreamReceiverOptions.builder().pollTimeout(Duration.ofMillis(100))
                .build();
        StreamReceiver<String, MapRecord<String, String, String>> receiver = StreamReceiver.create(reactiveRedisConnectionFactory, options);
        Flux<MapRecord<String, String, String>> flux = receiver.receive(StreamOffset.fromStart("mstream"))
                .doOnCancel(() -> System.out.println("Cancel"))
                .doOnComplete(() -> System.out.println("Complete"))
                .doOnTerminate(() -> System.out.println("Terminate"));
        flux.subscribeOn(Schedulers.newParallel("reactive"))
                .subscribe(o -> System.out.println(String.format("[thread: %s] reative-stream: %s, id: %s, k/v: %s",
                        Thread.currentThread().getName(), o.getStream(), o.getId(), o.getValue())));
        return receiver;
    }

}
