package cn.zero.cloud.platform.telemetry.logger.impl;

import cn.zero.cloud.platform.telemetry.logger.AbstractTelemetryLogger;
import cn.zero.cloud.platform.telemetry.logger.TelemetryLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Xisun Wang
 * @since 2024/3/26 10:52
 */
public class TelemetryNonQueueLoggerImpl extends AbstractTelemetryLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelemetryNonQueueLoggerImpl.class);

    private static final TelemetryLogger TELEMETRY_LOGGER_NON_QUEUE_INSTANCE = new TelemetryNonQueueLoggerImpl();

    private TelemetryNonQueueLoggerImpl() {
    }

    public static TelemetryLogger getTelemetryLoggerNonQueueInstance() {
        return TELEMETRY_LOGGER_NON_QUEUE_INSTANCE;
    }

    public void writeLog(String logAsJson) {
        LOGGER.info(logAsJson);
    }
}
