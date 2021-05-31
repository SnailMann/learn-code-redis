package com.snailmann.demo.redis.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfig {

    /**
     * Jedis连接池
     *
     * @return
     */
    @Bean
    public JedisPool getJedisPool() {  //Redis连接池连接
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        return new JedisPool(poolConfig, "127.0.0.1", 6379);
    }
}
