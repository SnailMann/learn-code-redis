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
     * 使用lua脚本组合了setnx和expire命令
     *
     * @param key
     * @param ttl
     * @return
     */
    public boolean tryLock(String key, String val, Long ttl) {
        Jedis jedis = jedisPool.getResource();
        try {
            //ResourceUtils获取file在Linux下会失效
            String lua = getLua("classpath:lua/trylock.lua");
            while (true) {
                //执行lua脚本，即将setnx操作和expire操作合并成一个原子操作
                long result = (long) jedis.eval(lua, 1, KEY + key, val, ttl + "");
                //当结果返回的是1，代表程序获得到锁，关闭jedis, 返回true
                if (result == 1) {
                    return true;
                }
            }
        } finally {
            jedis.close();
        }

    }


    /**
     * 释放锁
     * 这里使用了lua脚本，将get,del两个操作组合为一个原子操作
     * 这里实现了，只有拥有当前锁的线程才有资格释放锁的判断
     *
     * @param key
     * @return
     */
    public boolean releaseLock(String key, String val) {
        //总之就是要记得关闭jedis
        Jedis jedis = jedisPool.getResource();
        try {
            //获得lua脚本
            String lua = getLua("classpath:lua/releaselock.lua");
            //执行脚本，获得返回结果,1 是释放锁成功， 0 是释放锁不成功（当前线程不是持有锁的线程，无权释放）
            long result = (long) jedis.eval(lua, 1, KEY + key, val);
            return result > 0;
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            jedis.close();
        }

        return false;

    }

    /**
     * 获得resource下的lua脚本
     *
     * @param path
     * @return
     */
    public String getLua(String path) {
        try {
            InputStream input = new FileInputStream(ResourceUtils.getFile(path));
            byte[] by = new byte[input.available()];
            input.read(by);
            return new String(by);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
