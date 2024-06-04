package cn.zero.cloud.platform.telemetry.factory;

import cn.zero.cloud.platform.common.constants.TelemetryConstants;
import cn.zero.cloud.platform.telemetry.logger.manual.TelemetryCommonTypeLoggerImpl;

import static cn.zero.cloud.platform.common.constants.ExceptionConstants.UTILITY_CLASS;

/**
 * @author Xisun Wang
 * @since 2024/3/25 11:07
 */
public class TelemetryManualLoggerFactory {
    private TelemetryManualLoggerFactory() {
        throw new IllegalStateException(UTILITY_CLASS);
    }

    public static TelemetryCommonTypeLoggerImpl getTelemetryCommonTypeLogger(TelemetryConstants.ObjectType objectType, String objectID) {
        return new TelemetryCommonTypeLoggerImpl(objectType, objectID);
    }
}
