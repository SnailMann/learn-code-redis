package com.snailmann.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class RedisService {

    @Autowired
    Jedis jedis;   //直接连接

    @Autowired
    JedisPool jedisPool; //连接池连接

    public void redisTest() {

        jedisPool.getResource().zrange("demo.zset.1", 0, 9).forEach(System.out::println);
        jedisPool.close();
    }


    public void zsetTest() {
        for (int i = 0; i < 10; i++) {
            jedis.zadd("demo.zset.1", i, String.valueOf(new Random().nextInt(1000)));
            jedis.slowlogGet().forEach(s -> System.out.println("慢查询：" + s));
        }
        jedis.zrange("demo.zset.1", 0, 9).forEach(System.out::println);
        jedis.close();
    }


    /**
     * hash测试，单次分批插入1000次数据
     */
    public void hsetTest() {
        long startTime = System.currentTimeMillis();
        Jedis jedis = jedisPool.getResource();
        for (int i = 0; i < 10000; i++) {
            jedis.hset("hashkey:1", "field:" + i, "value" + i);
        }
        /*List<String> list = jedis.hmget("hashkey:3", ((Function<String, String[]>) (string -> {
            List<String> list1 = new ArrayList<>();
            System.out.println("前" + list1);
            for (int j = 0; j < 1000; j++) {
                list1.add(string + j);
            }
            System.out.println("后" + list1);
            return list1.toArray(new String[]{});
        })).apply("field:"));


        System.out.println(list);*/
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("hset执行时间：" + excTime + "s");
        jedis.close();
    }

    public void hmsetTest() {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            map.put("field:" + i, "value:" + i);
        }
        long startTime = System.currentTimeMillis();
        Jedis jedis = jedisPool.getResource();
        jedis.hmset("hashkey:2", map);

        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("hmset执行时间：" + excTime + "s");
        jedis.close();
    }

    /**
     * 通过pipeline来批量插入数据
     * <p>
     * hset     - 执行时间：1.141s
     * pipeline - 执行时间：0.024s
     */
    public void pipelineTest() {
        long startTime = System.currentTimeMillis();
        Jedis jedis = jedisPool.getResource();
        Pipeline pipeline = jedis.pipelined();
        for (int i = 0; i < 10000; i++) {
            pipeline.hset("hashkey:3", "field:" + i, "value" + i);
        }
        pipeline.syncAndReturnAll();

        jedis.close();
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("pipeline-hset执行时间：" + excTime + "s");
    }

}
