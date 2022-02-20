package com.snailmann.demo.redis.stream.message.producer;

import com.snailmann.demo.redis.stream.message.Event;
import com.snailmann.demo.redis.stream.message.consumer.StreamConsumer;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author liwenjie
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StreamProducerTest {


    @Autowired
    StreamProducer streamProducer;

    @Autowired
    StreamConsumer streamConsumer;

    @Test
    public void producer() {
        streamProducer.producer("mstream", Event.builder().id(RandomUtils.nextLong()).msg("test").build());
    }

    @Test
    public void consumer() {
        streamConsumer.consumer("mgroup", "mstream");
    }
}