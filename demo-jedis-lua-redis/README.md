# demo-jedis-lua-redis

通过Jedis客户端调用redis, 运用lua脚本

- [Spring Data Redis + Lua传送门](../demo-springboot-lua-redis)

## 场景

- 通过Lua脚本实现Redis分布式锁
- 通过Lua脚本实现列表分片

### Redis锁场景

我们知道Redis可以用于实现分布式锁，但是使用Redis分布式锁会存在一个问题，就是要解决多个原子操作组合并非原子性的问题
比如

- 加锁的时候，需要给锁一个过期时间，即setnx之后，需要expire一下key(我们这里不用set多参数做例子)
- 释放锁的时候，需要先查询该线程是否是该锁的持有者，如果不是则没有权利释放锁，通常用get和del做搭配

如果不能保证复合命令的原子性，就会导致在高并发场合下，出现不是我们想要的结果。甚至程序中断后，导致一些问题，比如加锁时，setnx之后，程序中断，导致锁没有过期时间，造成死锁问题等

我们知道Redis本身也提供了事务的方式来保证复合命令的原子性，那为什么还需要lua脚本呢，首先Redis本身的事务并不是完整的事务，有很多的缺陷，比如不支持回滚，另外Redis的事务的性能不好，所以
我们就可以使用lua来代替

#### 使用lua脚本实现加锁tryLock()

```lua
-- 获得锁，如果成功，就给锁一个超时时间
if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then
    redis.call('expire', KEYS[1], ARGV[2])
    return 1

else
    -- 如果没有获得锁，则返回失败
    return 0
end

```


#### 使用lua脚本实现加锁releaseLock()
```lua
-- 只有持有该锁的线程才能释放该锁，是为了避免锁过期了还有任务在跑的情况
if redis.call('get', KEYS[1]) == ARGV[1] then
    return redis.call('del', KEYS[1])
else
    return 0;

end
```


### Redis列表分片场景

 