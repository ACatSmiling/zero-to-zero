package cn.zero.cloud.platform.redis.service;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/20 22:46
 */
public interface RedissonApiService {
    void addElementToBloomFilter(Object key);

    void checkElementWithBloomFilter(Object key);
}
