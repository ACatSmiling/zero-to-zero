package cn.zero.cloud.platform.kafka.producer.ratelimit;

import cn.zero.cloud.platform.kafka.producer.cache.WebexKafkaCaffeineCacheManager;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class KafkaSenderRatelimiter {

    @Autowired
    WebexKafkaCaffeineCacheManager cacheManager;

    public boolean isBelowThreshold(String cacheName , int ratelimit, String key){
        if (ratelimit == 0) {
            return true;
        }
        CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(cacheName);
        Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
        AtomicInteger atomicInteger = (AtomicInteger) nativeCache.get(key, v -> new AtomicInteger(0));
        int sum = atomicInteger.addAndGet(1);
        //nativeCache.asMap().forEach((k,v) -> logger.info(cacheName + " : " + k + " -> " + v));
        return sum <= ratelimit;
    }
}
