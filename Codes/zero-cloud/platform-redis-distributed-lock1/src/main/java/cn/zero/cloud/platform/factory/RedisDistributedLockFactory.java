package cn.zero.cloud.platform.factory;

import cn.zero.cloud.platform.lock.RedisDistributedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/25 23:11
 */
@Component
public class RedisDistributedLockFactory {
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisDistributedLockFactory(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Lock getDistributedLock(String lockType, String lockName, Object lockValue, long expireTime) {
        if (lockType == null) {
            return null;
        }

        if ("REDIS".equalsIgnoreCase(lockType)) {
            return new RedisDistributedLock(redisTemplate, lockName, lockValue, expireTime);
        } else if ("ZOOKEEPER".equalsIgnoreCase(lockType)) {
            // TODO zookeeper版本的分布式锁实现
            return null;
        } else if ("MYSQL".equalsIgnoreCase(lockType)) {
            // TODO mysql版本的分布式锁实现
            return null;
        }

        return null;
    }
}
