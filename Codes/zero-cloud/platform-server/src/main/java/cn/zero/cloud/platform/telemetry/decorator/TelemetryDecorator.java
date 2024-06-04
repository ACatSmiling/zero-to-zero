package cn.zero.cloud.platform.telemetry.decorator;

import cn.zero.cloud.platform.telemetry.pojo.TelemetryLog;
import org.slf4j.MDC;

/**
 * 特殊日志处理装饰器
 *
 * @author Xisun Wang
 * @since 2024/3/14 17:23
 */
public interface TelemetryDecorator {
    void buildTelemetryItems(Object result, TelemetryLog telemetryLog);

    default void setCommonMessage(TelemetryLog telemetryLog) {
        //TODO 针对切面日志的通用处理
        telemetryLog.setRandomUUID(MDC.get("UUID_KEY"));
    }
}
