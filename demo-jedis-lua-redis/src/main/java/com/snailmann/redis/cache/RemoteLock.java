package com.snailmann.redis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;

/**
 * Jedis实现的分布式锁
 * 记得用完就要关闭jedis, 不然是会造成阻塞的，即jedis没有释放，导致其他的线程一直在等待从JedisPool获得连接
 */
@Component
public class RemoteLock {

    private final static String KEY = "";

    @Autowired
    JedisPool jedisPool;


    /**
     * 加锁
     *
     * @param key
     * @param ttl
     * @return
     */
    public boolean tryLock(String key, Long ttl) {
        Jedis jedis = jedisPool.getResource();
        try {
            String val = Thread.currentThread().getName();
            //ResourceUtils获取file在Linux下会失效
            String lua = getLua(ResourceUtils.getFile("classpath:lua/lock.lua"));
            while (true) {
                //执行lua脚本，即将setnx操作和expire操作合并成一个原子操作
                long result = (long) jedis.eval(lua, 1, KEY + key, val, ttl + "");
                //当结果返回的是1，代表程序获得到锁，关闭jedis, 返回true
                if (result == 1) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }

        return false;

    }


    /**
     * 释放锁
     * 这里没有做是否是锁的持有者来释放该锁，因为本质上，比较少会出现这种问题
     * 严苛条件下还是建议判断，也可以通过lua脚本来实现
     *
     * @param key
     * @return
     */
    public boolean releaseLock(String key) {
        //总之就是要记得关闭jedis
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.del(KEY + key) > 0;
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            jedis.close();
        }

        return false;

    }

    public String getLua(File file) {

        try {
            InputStream input = new FileInputStream(file);
            byte[] by = new byte[input.available()];
            input.read(by);
            return new String(by);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
