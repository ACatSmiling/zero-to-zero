package cn.zero.cloud.platform.controller;

import cn.zero.cloud.platform.telemetry.Telemetry;
import cn.zero.cloud.platform.utils.PlatFormJsonUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static cn.zero.cloud.platform.common.constants.TelemetryConstants.FeatureType.TEST_FEATURE;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.MetricType.TEST_METRIC;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.ModuleType.TEST_API;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.ObjectType.TEST_OBJECT;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.VerbType.SELECT;

/**
 * @author Xisun Wang
 * @since 6/14/2024 09:59
 */
@Slf4j
@RestController
@RequestMapping(value = "/cache")
public class CacheController {
    private final CaffeineCacheManager caffeineCacheManager;

    @Autowired
    public CacheController(CaffeineCacheManager caffeineCacheManager) {
        this.caffeineCacheManager = caffeineCacheManager;
    }

    @Telemetry(moduleType = TEST_API, metricType = TEST_METRIC, featureType = TEST_FEATURE, verbType = SELECT, objectType = TEST_OBJECT)
    @GetMapping(value = "/caffeine/check", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void checkCaffeineCache(@RequestParam String cacheName, @RequestParam String key) {
        // CaffeineCacheManager是内存级别的缓存，当程序停止后，缓存会消失，cacheName即为@Cacheable注解的value属性
        Cache cache = caffeineCacheManager.getCache(cacheName);
        if (cache != null) {
            Book book = cache.get(key, Book.class);
            if (book != null) {
                log.info("Cache hit for key: {}, value: {}", key, PlatFormJsonUtil.serializeToJson(book));
            } else {
                log.info("Cache miss for key: {}", key);
            }
        } else {
            log.info("Cache miss for cacheName: {}", cacheName);
        }
    }

    @Telemetry(moduleType = TEST_API, metricType = TEST_METRIC, featureType = TEST_FEATURE, verbType = SELECT, objectType = TEST_OBJECT)
    @GetMapping(value = "/caffeine/test", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Cacheable(value = "BOOKS", key = "#key", cacheManager = "caffeineCacheManager") // cacheManager指定使用非默认的缓存管理器
    public Book testCaffeineCache(@RequestParam String key) {
        // 第一次调用方法时，会进入方法体，使用@Cacheable注解后，第二次调用方法不会进入方法体
        log.info("caffeine cache manager test, key: {}", key);
        return Book.builder().id("abcd").name("平凡的世界").author("路遥").timestamp(System.currentTimeMillis()).build();
    }

    @Telemetry(moduleType = TEST_API, metricType = TEST_METRIC, featureType = TEST_FEATURE, verbType = SELECT, objectType = TEST_OBJECT)
    @GetMapping(value = "/redis/test", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    // @Cacheable注解通常用在方法上，表示该方法的结果是可缓存的。如果缓存中存在之前执行过的结果，那么方法不会再次执行，而是直接从缓存中返回结果
    @Cacheable(value = "BOOKS", key = "#key")
    public Book testRedisCache(@RequestParam String key) {
        // 第一次调用方法时，会进入方法体，使用@Cacheable注解后，第二次调用方法不会进入方法体
        log.info("redis cache manager test, key: {}", key);
        return Book.builder().id("abcd").name("平凡的世界").author("路遥").timestamp(System.currentTimeMillis()).build();
    }

    @Telemetry(moduleType = TEST_API, metricType = TEST_METRIC, featureType = TEST_FEATURE, verbType = SELECT, objectType = TEST_OBJECT)
    @GetMapping(value = "/redis/test/put", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    // @CachePut注解确保方法始终被执行，并且其结果放入缓存中，无论缓存中是否已经存在相同键的条目
    @CachePut(value = "BOOKS", key = "#key")
    public Book testCachePut(@RequestParam String key) {
        // 每次调用方法时，都会进入方法体
        log.info("redis cache manager test @CachePut, key: {}", key);
        return Book.builder().id("abcd").name("平凡的世界").author("路遥").timestamp(System.currentTimeMillis()).build();
    }

    @Telemetry(moduleType = TEST_API, metricType = TEST_METRIC, featureType = TEST_FEATURE, verbType = SELECT, objectType = TEST_OBJECT)
    @GetMapping(value = "/redis/test/evict", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    // @CacheEvict注解用于从缓存中移除条目。这通常用于删除操作，确保缓存不会返回过时的数据
    // @CacheEvict还有一个allEntries属性，如果设置为true，则会清除缓存中所有的条目
    @CacheEvict(value = "BOOKS", key = "#key")
    public Book testCacheEvict(@RequestParam String key) {
        // 已经存在的缓存，在调用此方法时，会删除对应的缓存
        log.info("redis cache manager test @CacheEvict, key: {}", key);
        return Book.builder().id("abcd").name("平凡的世界").author("路遥").timestamp(System.currentTimeMillis()).build();
    }

    @Telemetry(moduleType = TEST_API, metricType = TEST_METRIC, featureType = TEST_FEATURE, verbType = SELECT, objectType = TEST_OBJECT)
    @GetMapping(value = "/redis/test/combination", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    // 组合注解，假设key为abcd。慎用，例如@Cacheable和@CachePut一起，方法每次调用都会执行，失去了@Cacheable的作用
    @Caching(
            // 每次调用接口时，abcd第一次加入缓存，后续从缓存取值
            cacheable = @Cacheable(value = "BOOKS", key = "#key"),
            // 每次调用接口时，abcd_1和abcd_2都更新缓存
            put = {@CachePut(value = "BOOKS", key = "#key+'_1'"), @CachePut(value = "BOOKS", key = "#key+'_2'")},
            // 每次调用接口时，abcd_3和abcd_4都删除缓存
            evict = {@CacheEvict(value = "BOOKS", key = "#key+'_3'"), @CacheEvict(value = "BOOKS", key = "#key+'_4'")}
    )
    public Book testCacheCombination(@RequestParam String key) {
        log.info("redis cache manager test @Caching, key: {}", key);
        return Book.builder().id("abcd").name("平凡的世界").author("路遥").timestamp(System.currentTimeMillis()).build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class Book {
        private String id;

        private String name;

        private String author;

        private long timestamp;
    }
}
