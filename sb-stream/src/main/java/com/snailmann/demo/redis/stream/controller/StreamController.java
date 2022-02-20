package com.snailmann.demo.redis.stream.controller;

import com.snailmann.demo.redis.stream.message.Event;
import com.snailmann.demo.redis.stream.message.producer.StreamProducer;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liwenjie
 */
@RestController
@RequestMapping("/springboot-stream/stream/")
public class StreamController {

    private final StreamProducer streamProducer;

    public StreamController(StreamProducer streamProducer) {
        this.streamProducer = streamProducer;
    }

    @GetMapping("/xadd")
    public String xadd(
            @RequestParam(required = false, defaultValue = "mstream") String stream,
            @RequestParam(required = false, defaultValue = "test") String message) {
        streamProducer.producer(stream, Event.builder().id(RandomUtils.nextLong(1, 10000)).msg(message).build());
        return "success";
    }
}
