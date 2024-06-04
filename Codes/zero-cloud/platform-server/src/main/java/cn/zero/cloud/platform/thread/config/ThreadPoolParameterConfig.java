package cn.zero.cloud.platform.thread.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 线程池参数配置
 *
 * @author Xisun Wang
 * @since 2024/4/12 17:17
 */
@ConfigurationProperties(prefix = "platform.thread.pool")
@Component
public class ThreadPoolParameterConfig {
    private int highConcurrentThreadSize;

    private int lowConcurrentThreadSize;

    private int keepAliveSeconds;

    public int getHighConcurrentThreadSize() {
        if (highConcurrentThreadSize == 0) {
            // 高并发，核心线程池：20个
            highConcurrentThreadSize = 20;
        }
        return highConcurrentThreadSize;
    }

    public void setHighConcurrentThreadSize(int highConcurrentThreadSize) {
        this.highConcurrentThreadSize = highConcurrentThreadSize;
    }

    public int getLowConcurrentThreadSize() {
        if (lowConcurrentThreadSize == 0) {
            // 低并发，核心线程池：6个
            lowConcurrentThreadSize = 6;
        }
        return lowConcurrentThreadSize;
    }

    public void setLowConcurrentThreadSize(int lowConcurrentThreadSize) {
        this.lowConcurrentThreadSize = lowConcurrentThreadSize;
    }

    public int getKeepAliveSeconds() {
        if (keepAliveSeconds == 0) {
            // 保持存活时间：600s
            keepAliveSeconds = 600;
        }
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }
}
