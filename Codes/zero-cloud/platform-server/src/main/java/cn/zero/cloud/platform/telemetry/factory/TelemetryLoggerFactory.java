package cn.zero.cloud.platform.telemetry.factory;

import cn.zero.cloud.platform.telemetry.logger.AbstractTelemetryLogger;
import cn.zero.cloud.platform.telemetry.filter.FeatureNameFilter;
import cn.zero.cloud.platform.telemetry.logger.TelemetryLogger;
import cn.zero.cloud.platform.telemetry.logger.impl.TelemetryLoggerImpl;
import cn.zero.cloud.platform.telemetry.logger.impl.TelemetryNonQueueLoggerImpl;

import static cn.zero.cloud.platform.common.constants.ExceptionConstants.UTILITY_CLASS;

/**
 * @author Xisun Wang
 * @since 2024/3/21 12:11
 */
public class TelemetryLoggerFactory {
    private TelemetryLoggerFactory() {
        throw new IllegalStateException(UTILITY_CLASS);
    }

    public static TelemetryLogger getTelemetryLogger() {
        return TelemetryLoggerImpl.getTelemetryLoggerInstance();
    }

    public static TelemetryLogger getTelemetryLogger(boolean enableInternalQueue) {
        return enableInternalQueue ? TelemetryLoggerImpl.getTelemetryLoggerInstance() :
                TelemetryNonQueueLoggerImpl.getTelemetryLoggerNonQueueInstance();
    }

    public static void initFilter(FeatureNameFilter filter) {
        AbstractTelemetryLogger.setFilter(filter);
    }
}
