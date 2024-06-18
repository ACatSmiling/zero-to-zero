package cn.zero.cloud.platform.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Xisun Wang
 * @since 6/18/2024 10:16
 */
@ConfigurationProperties(prefix = "zero.cloud.security")
@Component
public class SecurityProperties {
    private boolean debug;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
