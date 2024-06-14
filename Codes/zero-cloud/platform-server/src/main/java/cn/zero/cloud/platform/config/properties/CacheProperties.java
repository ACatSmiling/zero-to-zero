package cn.zero.cloud.platform.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Xisun Wang
 * @since 6/13/2024 17:28
 */
@ConfigurationProperties(prefix = "zero.cloud.cache")
@Component
public class CacheProperties {
    private String cacheType;

    private String cacheName;

    private long cacheDuration;

    private String cachePrefix;

    public String getCacheType() {
        return cacheType;
    }

    public void setCacheType(String cacheType) {
        this.cacheType = cacheType;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public long getCacheDuration() {
        return cacheDuration;
    }

    public void setCacheDuration(long cacheDuration) {
        this.cacheDuration = cacheDuration;
    }

    public String getCachePrefix() {
        return cachePrefix;
    }

    public void setCachePrefix(String cachePrefix) {
        this.cachePrefix = cachePrefix;
    }
}
