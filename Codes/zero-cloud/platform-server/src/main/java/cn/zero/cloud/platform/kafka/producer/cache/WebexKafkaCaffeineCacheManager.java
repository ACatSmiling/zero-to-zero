package cn.zero.cloud.platform.kafka.producer.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Component("webexKafkaCaffeineCacheManager")
@EnableConfigurationProperties({KafkaCaffeineCacheSpecs.class})
public class WebexKafkaCaffeineCacheManager implements CacheManager {

    @Autowired
    private KafkaCaffeineCacheSpecs specs;

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>(16);

    @Override
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(this.cacheMap.keySet());
	}

	@Override
	public Cache getCache(String name) {
        return this.cacheMap.computeIfAbsent(name, this::createCaffeineCache);
	}


    protected Cache createCaffeineCache(String name) {
        return new CaffeineCache(name, createNativeCaffeineCache(name), true);
    }

    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
		String cacheSpecification = specs.get(name);
		return Caffeine.from(cacheSpecification).build();
	}

}
