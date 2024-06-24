package cn.zero.cloud.platform.service.impl;

import cn.zero.cloud.platform.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/21 22:22
 */
@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {
    @Value("${server.port}")
    private String port;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public InventoryServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 改进版本五：使用LUA脚本，保证释放锁过程的原子性
    @Override
    public String sale() {
        String retMessage = "";
        String key = "RedisDistributedLock";
        String uuidValue = UUID.randomUUID() + ":" + Thread.currentThread().getId();

        // 加锁和设置锁的过期时间，必须保证原子性，不能分开写：redisTemplate.opsForValue().setIfAbsent(key, uuidValue);和redisTemplate.expire(key, 30L, TimeUnit.SECONDS);
        while (Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(key, uuidValue, 30L, TimeUnit.SECONDS))) {
            // 暂停20毫秒，类似CAS自旋
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException e) {
                log.info("thread sleep error: ", e);
            }
        }
        try {
            // 1 查询库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory001");
            // 2 判断库存是否足够
            int inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            // 3 扣减库存
            if (inventoryNumber > 0) {
                redisTemplate.opsForValue().set("inventory001", String.valueOf(--inventoryNumber));
                retMessage = "成功卖出一个商品，库存剩余: " + inventoryNumber;
            } else {
                retMessage = "商品卖完了";
            }
            // log.info("retMessage: {}", retMessage);
        } finally {
            // 将判断+删除自己的合并为lua脚本保证原子性
            String luaScript =
                    "if (redis.call('get',KEYS[1]) == ARGV[1]) then " +
                            "return redis.call('del',KEYS[1]) " +
                            "else " +
                            "return 0 " +
                            "end";
            Boolean execute = redisTemplate.execute(new DefaultRedisScript<>(luaScript, Boolean.class), List.of(key), uuidValue);
            log.info("Trace of redis distributed lock, release lock: {}", execute);
        }
        return retMessage + "\t" + "服务端口号：" + port;
    }

    /*
    // 改进版本四：只能释放当前线程设置的锁，不能误删其他线程的锁
    @Override
    public String sale() {
        String retMessage = "";
        String key = "RedisDistributedLock";
        String uuidValue = UUID.randomUUID() + ":" + Thread.currentThread().getId();

        // 加锁和设置锁的过期时间，必须保证原子性，不能分开写：redisTemplate.opsForValue().setIfAbsent(key, uuidValue);和redisTemplate.expire(key, 30L, TimeUnit.SECONDS);
        while (Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(key, uuidValue, 30L, TimeUnit.SECONDS))) {
            // 暂停20毫秒，类似CAS自旋
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException e) {
                log.info("thread sleep error: ", e);
            }
        }
        try {
            // 1 查询库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory001");
            // 2 判断库存是否足够
            int inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            // 3 扣减库存
            if (inventoryNumber > 0) {
                redisTemplate.opsForValue().set("inventory001", String.valueOf(--inventoryNumber));
                retMessage = "成功卖出一个商品，库存剩余: " + inventoryNumber;
            } else {
                retMessage = "商品卖完了";
            }
            log.info("retMessage: {}", retMessage);
        } finally {
            // 判断加锁与解锁是不是同一个客户端，同一个才行，判断value值，自己只能删除自己的锁，不误删他人的
            String value = (String) redisTemplate.opsForValue().get(key);
            if (StringUtils.isNotBlank(value) && value.equalsIgnoreCase(uuidValue)) {
                redisTemplate.delete(key);
            }
        }
        return retMessage + "\t" + "服务端口号：" + port;
    }*/

    /*
    // 改进版本三：设置锁的过期时间，并保证和加锁操作的原子性
    @Override
    public String sale() {
        String retMessage = "";
        String key = "RedisDistributedLock";
        String uuidValue = UUID.randomUUID() + ":" + Thread.currentThread().getId();

        // 加锁和设置锁的过期时间，必须保证原子性，不能分开写：redisTemplate.opsForValue().setIfAbsent(key, uuidValue);和redisTemplate.expire(key, 30L, TimeUnit.SECONDS);
        while (Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(key, uuidValue, 30L, TimeUnit.SECONDS))) {
            // 暂停20毫秒，类似CAS自旋
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException e) {
                log.info("thread sleep error: ", e);
            }
        }
        try {
            // 1 查询库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory001");
            // 2 判断库存是否足够
            int inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            // 3 扣减库存
            if (inventoryNumber > 0) {
                redisTemplate.opsForValue().set("inventory001", String.valueOf(--inventoryNumber));
                retMessage = "成功卖出一个商品，库存剩余: " + inventoryNumber;
            } else {
                retMessage = "商品卖完了";
            }
            log.info("retMessage: {}", retMessage);
        } finally {
            redisTemplate.delete(key);
        }
        return retMessage + "\t" + "服务端口号：" + port;
    }*/

    /*
    // 改进版本二：使用while替换if，自旋替换递归重试。存在问题：程序异常可能导致finally模块代码不能正常执行，进而导致锁不能正常释放，需要给锁添加过期时间
    @Override
    public String sale() {
        String retMessage = "";
        String key = "RedisDistributedLock";
        String uuidValue = UUID.randomUUID() + ":" + Thread.currentThread().getId();

        while (Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(key, uuidValue))) {
            // 暂停20毫秒，类似CAS自旋
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException e) {
                log.info("thread sleep error: ", e);
            }
        }
        try {
            // 1 查询库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory001");
            // 2 判断库存是否足够
            int inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            // 3 扣减库存
            if (inventoryNumber > 0) {
                redisTemplate.opsForValue().set("inventory001", String.valueOf(--inventoryNumber));
                retMessage = "成功卖出一个商品，库存剩余: " + inventoryNumber;
            } else {
                retMessage = "商品卖完了";
            }
            log.info("retMessage: {}", retMessage);
        } finally {
            redisTemplate.delete(key);
        }
        return retMessage + "\t" + "服务端口号：" + port;
    }*/

    /*
    // 改进版本一：使用递归重试的方式，不断获取锁，直到成功。存在问题：递归容易导致StackOverflowError，不推荐
    @Override
    public String sale() {
        String retMessage = "";
        String key = "RedisDistributedLock";
        String uuidValue = UUID.randomUUID() + ":" + Thread.currentThread().getId();

        Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, uuidValue);
        if (Boolean.FALSE.equals(flag)) {
            // 暂停20毫秒后递归调用
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException e) {
                log.info("thread sleep error: ", e);
            }

            // 如果没拿到锁，则递归重试
            sale();
        } else {
            try {
                // 1 查询库存信息
                String result = (String) redisTemplate.opsForValue().get("inventory001");
                // 2 判断库存是否足够
                int inventoryNumber = result == null ? 0 : Integer.parseInt(result);
                // 3 扣减库存
                if (inventoryNumber > 0) {
                    redisTemplate.opsForValue().set("inventory001", String.valueOf(--inventoryNumber));
                    retMessage = "成功卖出一个商品，库存剩余: " + inventoryNumber;
                } else {
                    retMessage = "商品卖完了";
                }
                log.info("retMessage: {}", retMessage);
            } finally {
                redisTemplate.delete(key);
            }
        }
        return retMessage + "\t" + "服务端口号：" + port;
    }*/

    /*
    // 原始版本：对于分布式服务，Synchronized或者Lock这种JVM级别的锁不再适用，会出现超卖的异常情况。

    private final Lock lock = new ReentrantLock();

    @Override
    public String sale() {
        String retMessage;
        lock.lock();
        try {
            // 1 查询库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory001");
            // 2 判断库存是否足够
            int inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            // 3 扣减库存
            if (inventoryNumber > 0) {
                redisTemplate.opsForValue().set("inventory001", String.valueOf(--inventoryNumber));
                retMessage = "成功卖出一个商品，库存剩余: " + inventoryNumber;
            } else {
                retMessage = "商品卖完了";
            }
            log.info("retMessage: {}", retMessage);
        } finally {
            lock.unlock();
        }
        return retMessage + "\t" + "服务端口号：" + port;
    }*/
}
