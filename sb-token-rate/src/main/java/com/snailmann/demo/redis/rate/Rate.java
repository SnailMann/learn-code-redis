package com.snailmann.demo.redis.rate;

/**
 * @author liwenjie
 */
public interface Rate {

    boolean getToken(String key);

}
