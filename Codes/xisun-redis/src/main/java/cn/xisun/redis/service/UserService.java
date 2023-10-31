package cn.xisun.redis.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author XiSun
 * @since 2023/10/27 20:42
 */
@Slf4j
@Service
public class UserService {

    /*public static final String CACHE_KEY_USER = "user:";

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate redisTemplate;

    *//**
     * 此方法中，业务逻辑没有写错，对于小厂中厂(QPS ≤ 1000)可以使用，但是大厂不行
     *
     * @param id
     * @return
     *//*
    public User findUserById(Integer id) {
        User user = null;
        String key = CACHE_KEY_USER + id;

        // 1 先从Redis里面查询，如果有直接返回结果，如果没有再去查询MySQL
        user = (User) redisTemplate.opsForValue().get(key);

        if (user == null) {
            // 2 Redis里面无数据，继续查询MySQL
            user = userMapper.selectByPrimaryKey(id);
            if (user == null) {
                // 3.1 Redis和MySQL都无数据
                // 可以具体细化，防止多次穿透，比如业务规定，记录下导致穿透的这个key回写Redis
                return user;
            } else {
                // 3.2 MySQL有，将数据写回Redis，保证下一次的缓存命中率
                redisTemplate.opsForValue().set(key, user);
            }
        }
        return user;
    }


    *//**
     * 加强补充，避免突然key失效了，打爆MySQL，做一下预防，尽量不出现击穿的情况
     *
     * @param id
     * @return
     *//*
    public User findUserById2(Integer id) {
        User user = null;
        String key = CACHE_KEY_USER + id;

        // 1 先从Redis里面查询，如果有直接返回结果，如果没有再去查询MySQL
        // 第1次查询Redis，加锁前
        user = (User) redisTemplate.opsForValue().get(key);

        if (user == null) {
            // 2 大厂用，对于高QPS的优化，进来就先加锁，保证一个请求操作，让外面的Redis等待一下，避免击穿MySQL
            synchronized (UserService.class) {
                // 第2次查询Redis，加锁后
                user = (User) redisTemplate.opsForValue().get(key);
                // 3 二次查Redis还是null，可以去查MySQL了(MySQL默认有数据)
                if (user == null) {
                    // 4 查询MySQL拿数据
                    user = userMapper.selectByPrimaryKey(id);
                    if (user == null) {
                        return null;
                    } else {
                        // 5 MySQL里面有数据的，需要回写Redis，完成数据一致性的同步工作
                        redisTemplate.opsForValue().setIfAbsent(key, user, 7L, TimeUnit.DAYS);
                    }
                }
            }
        }
        return user;
    }*/
}
