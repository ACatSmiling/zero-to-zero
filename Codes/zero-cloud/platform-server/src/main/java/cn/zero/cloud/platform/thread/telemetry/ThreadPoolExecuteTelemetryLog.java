package cn.zero.cloud.platform.thread.telemetry;

import cn.zero.cloud.platform.common.constants.TelemetryConstants;
import cn.zero.cloud.platform.telemetry.factory.TelemetryLoggerFactory;
import cn.zero.cloud.platform.telemetry.logger.TelemetryLogger;
import cn.zero.cloud.platform.telemetry.pojo.TelemetryLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ThreadPool Execute TelemetryLog
 *
 * @author Xisun Wang
 * @since 2024/4/12 17:17
 */
public class ThreadPoolExecuteTelemetryLog extends TelemetryLog {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolExecuteTelemetryLog.class);

    private static final TelemetryLogger TELEMETRY_LOGGER = TelemetryLoggerFactory.getTelemetryLogger(false);

    private String taskRunner;

    private String trackingID;

    private long costTime;

    private long waitTime;

    private String executeResult = "success";

    private String failReason;

    public ThreadPoolExecuteTelemetryLog() {
        super();
        this.setMetricType(TelemetryConstants.MetricType.THREAD_POOL_TASK_EXECUTE.getName());
        this.setFeatureType(TelemetryConstants.FeatureType.THREAD_POOL_TASK_EXECUTE.getName());
    }

    public String getTaskRunner() {
        return taskRunner;
    }

    public void setTaskRunner(String taskRunner) {
        this.taskRunner = taskRunner;
    }

    public String getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(String executeResult) {
        this.executeResult = executeResult;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
    }

    public void writeLog() {
        try {
            TELEMETRY_LOGGER.info(this);
        } catch (Exception exception) {
            LOGGER.error("fail to write thread pool execute telemetry log, exception: ", exception);
        }
    }
}
