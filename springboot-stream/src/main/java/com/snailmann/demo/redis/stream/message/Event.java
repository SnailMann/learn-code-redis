package com.snailmann.demo.redis.stream.message;

import lombok.Builder;
import lombok.Data;

/**
 * @author liwenjie
 */
@Data
@Builder
public class Event {

    private Long id;

    private String msg;
}
