# demo-springboot-lua-redis

通过spring data redis + lua 操作redis

## Spring Data Redis + Lua

加锁
```java
    /**
     * 加锁
     * 如果没有获取到锁，这里是无限循环
     * 可以自行优化重试的次数和重试的间隔时间
     * @param key
     * @param val
     * @param ttl
     * @return
     */
    public boolean tryLock(String key, String val, long ttl) {
        List<String> keys = Stream.of(KEY + key).collect(Collectors.toList());
        RedisScript<Long> SETNX_EXPIRE_SCRIPT = new DefaultRedisScript<>(getLua("classpath:lua/trylock.lua"), Long.class);

        while (true) {
            Long result = redisTemplate.execute(SETNX_EXPIRE_SCRIPT, keys, val, ttl + "");
            if (result != null && result.equals(1L)) {
                return true;
            }
        }
    }
```
释放锁
```java
    /**
     * 释放锁
     *
     * @param key
     * @param val
     * @return
     */
    public boolean releaseLock(String key, String val) {
        List<String> keys = Stream.of(KEY + key).collect(Collectors.toList());
        RedisScript<Long> GET_DEL_SCRIPT = new DefaultRedisScript<>(getLua("classpath:lua/releaseLock.lua"), Long.class);
        Long result = redisTemplate.execute(GET_DEL_SCRIPT, keys, val);
        return result != null && result > 0;
    }
```