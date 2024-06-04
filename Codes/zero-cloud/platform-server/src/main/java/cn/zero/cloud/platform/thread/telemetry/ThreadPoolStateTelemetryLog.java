package cn.zero.cloud.platform.thread.telemetry;

import cn.zero.cloud.platform.common.constants.TelemetryConstants;
import cn.zero.cloud.platform.telemetry.factory.TelemetryLoggerFactory;
import cn.zero.cloud.platform.telemetry.logger.TelemetryLogger;
import cn.zero.cloud.platform.telemetry.pojo.TelemetryLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ThreadPool State TelemetryLog
 *
 * @author Xisun Wang
 * @since 2024/4/12 17:17
 */
public class ThreadPoolStateTelemetryLog extends TelemetryLog {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolStateTelemetryLog.class);

    private static final TelemetryLogger TELEMETRY_LOGGER = TelemetryLoggerFactory.getTelemetryLogger(false);

    private int corePoolSize;

    private int maxPoolSize;

    private int currentPoolSize;

    private int currentTaskCount;

    private int queueWaitingCount;

    public ThreadPoolStateTelemetryLog(String poolName) {
        super();
        this.setMetricType(TelemetryConstants.MetricType.THREAD_POOL_WORK_STATE.getName());
        this.setFeatureType(poolName);
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getCurrentPoolSize() {
        return currentPoolSize;
    }

    public void setCurrentPoolSize(int currentPoolSize) {
        this.currentPoolSize = currentPoolSize;
    }

    public int getCurrentTaskCount() {
        return currentTaskCount;
    }

    public void setCurrentTaskCount(int currentTaskCount) {
        this.currentTaskCount = currentTaskCount;
    }

    public int getQueueWaitingCount() {
        return queueWaitingCount;
    }

    public void setQueueWaitingCount(int queueWaitingCount) {
        this.queueWaitingCount = queueWaitingCount;
    }

    public void writeLog() {
        try {
            TELEMETRY_LOGGER.info(this);
        } catch (Exception exception) {
            LOGGER.error("fail to write thread pool state telemetry log, exception: ", exception);
        }
    }
}
