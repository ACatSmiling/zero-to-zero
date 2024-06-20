package cn.zero.cloud.platform.redis.service.impl;

import cn.zero.cloud.platform.redis.service.RedissonApiService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.fenum.qual.PolyFenum;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/20 22:47
 */
@Slf4j
@Service
public class RedissonApiServiceImpl implements RedissonApiService {
    private final RedissonClient redissonClient;

    private RBloomFilter<Object> sampleBloomFilter;

    @Autowired
    public RedissonApiServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @PostConstruct
    public void init() {
        sampleBloomFilter = redissonClient.getBloomFilter("sampleBloomFilter");
        // 预计插入量
        long expectedInsertions = 10000;
        // 误判率
        double falseProbability = 0.03;
        // 初始化布隆过滤器，如果布隆过滤器尚未初始化过
        sampleBloomFilter.tryInit(expectedInsertions, falseProbability);
    }

    @Override
    public void addElementToBloomFilter(Object key) {
        // 添加元素到布隆过滤器
        sampleBloomFilter.add(key);
    }

    @Override
    public void checkElementWithBloomFilter(Object key) {
        // 检查元素是否可能存在于布隆过滤器中
        boolean result = sampleBloomFilter.contains(key);
        if (result) {
            log.info("{} element may exist in the bloom filter", key);
        } else {
            log.info("{} element is definitely not present in the bloom filter", key);
        }
    }
}
