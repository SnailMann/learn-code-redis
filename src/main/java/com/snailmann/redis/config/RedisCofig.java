package com.snailmann.redis.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class RedisCofig {

    @Bean
    public Jedis getJedis(){  //Redis直连
        return new Jedis("127.0.0.1",6379);
    }


    //貌似不能多个连接池同时存在，必须关掉一个，不然会报socket write error
    @Bean
    public JedisPool getJedisPool(){  //Redis连接池连接
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        return new JedisPool(poolConfig,"127.0.0.1",6379);
    }


    @Bean
    public JedisSentinelPool getJedisSentinelPool(){
        String masterName = "mymaster";
        Set<String> sentinels = new HashSet<>();
        sentinels.add("127.0.0.1:26379");
        sentinels.add("127.0.0.1:26380");
        sentinels.add("127.0.0.1:26381");
        return new JedisSentinelPool(masterName, sentinels);
    }


}
