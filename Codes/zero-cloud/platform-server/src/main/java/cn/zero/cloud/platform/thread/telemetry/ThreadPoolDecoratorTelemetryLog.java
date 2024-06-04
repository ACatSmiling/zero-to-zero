package cn.zero.cloud.platform.thread.telemetry;

import java.lang.reflect.ParameterizedType;

/**
 * 包含Decorator的ThreadPool TelemetryLog
 *
 * @author Xisun Wang
 * @since 2024/4/12 17:17
 */
public abstract class ThreadPoolDecoratorTelemetryLog<T extends ThreadPoolExecuteTelemetryLog> {
    public final Class<T> getTClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public abstract void decorate(T telemetryLog);
}
