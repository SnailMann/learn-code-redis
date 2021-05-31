# demo-springboot-lua-redis

通过spring data redis + lua 操作redis

- [Jedis + Lua传送门](../demo-jedis-lua-redis/README.md)

## Spring Data Redis + Lua场景

- Redis分布式锁
- Redis列表分片
- Redis简单限流（计数限流）

### Redis分布式锁场景

- 加锁
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
- 释放锁
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


### Redis列表分片场景


### Redis计数限流场景

- Lua脚本
```java
-- Redis简单限流

local key = KEYS[1]
local limitNum = tonumber(ARGV[1])
local expireTime = ARGV[2]

-- 如果key存在，则val + 1
if redis.call('exists',key) == 1 then
    -- 如果expireTime时间内，val大于limit数，则启动限流，返回0
    if redis.call('incr',key) > limitNum then
        return 0
    else
        return 1
    end
else
-- 如果不存在key , 则初始化， val = 1
    redis.call('set',key,1)
    redis.call('expire',key,expireTime)
    return 1
end
```

- 限流注解RateLimit

```java
/**
 * 限流注解
 * 被该注解修饰的方法，会启动限流策略
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流的Redis Key
     *
     * @return
     */
    String key() default "";

    /**
     * 达多少次数后限流
     *
     * @return
     */
    int limitNum() default 5;


    /**
     * ttl秒内，达limitNum次数后启动限流
     *
     * @return
     */
    long ttl() default 60;
}
```
