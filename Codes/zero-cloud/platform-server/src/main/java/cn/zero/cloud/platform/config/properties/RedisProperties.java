package cn.zero.cloud.platform.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Xisun Wang
 * @since 6/7/2024 16:47
 */
@ConfigurationProperties(prefix = "zero.cloud.redis")
@Component
public class RedisProperties {
    private boolean enableRedis;

    public boolean isEnableRedis() {
        return enableRedis;
    }

    public void setEnableRedis(boolean enableRedis) {

        this.enableRedis = enableRedis;
    }
}
