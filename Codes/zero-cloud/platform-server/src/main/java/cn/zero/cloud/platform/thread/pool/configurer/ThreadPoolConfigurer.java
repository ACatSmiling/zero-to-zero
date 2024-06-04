package cn.zero.cloud.platform.thread.pool.configurer;

import cn.zero.cloud.platform.thread.telemetry.ThreadPoolDecoratorTelemetryLog;
import cn.zero.cloud.platform.thread.local.transmitter.ThreadPoolThreadLocalTransmitter;

import java.util.List;

/**
 * 自定义的线程池ThreadLocalTransmitters和TelemetryLogDecorator接口
 *
 * @author Xisun Wang
 * @since 2024/4/12 17:17
 */
public interface ThreadPoolConfigurer {
    /**
     * 添加ThreadLocalTransmitters
     *
     * @param transmitters ThreadLocalTransmitters
     */
    default void addThreadPoolThreadLocalTransmitters(List<ThreadPoolThreadLocalTransmitter> transmitters) {
    }

    /**
     * 获取Decorator TelemetryLog
     *
     * @return TelemetryLogDecorator
     */
    default ThreadPoolDecoratorTelemetryLog getThreadPoolTelemetryLogDecorator() {
        return null;
    }
}
