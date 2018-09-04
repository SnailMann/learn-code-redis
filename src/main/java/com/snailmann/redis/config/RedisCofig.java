package com.snailmann.redis.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisCofig {

    @Bean
    public Jedis getJedis(){  //Redis直连
        return new Jedis("127.0.0.1",6379);
    }

    @Bean
    public JedisPool getJedisPool(){  //Redis连接池连接
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        return new JedisPool(poolConfig,"127.0.0.1",6379);
    }


}
