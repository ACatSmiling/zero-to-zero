package cn.zero.cloud.platform.kafka.common.telemetry.pojo;

import cn.zero.cloud.platform.kafka.common.telemetry.TelemetryConstants;
import cn.zero.cloud.platform.kafka.common.telemetry.TelemetryStatus;
import cn.zero.cloud.platform.telemetry.logger.TelemetryLogger;
import cn.zero.cloud.platform.telemetry.factory.TelemetryLoggerFactory;
import cn.zero.cloud.platform.telemetry.pojo.TelemetryLog;

import java.util.HashMap;

/**
 * @author Xisun Wang
 * @since 2024/3/11 9:24
 */
public class KafkaCommonTelemetryLog extends TelemetryLog {
    private String topicName;

    private String messageVersion;

    private String messageUUID;

    private String trackingID;

    private String operationType;

    private String actionType;

    private String resourceID;

    private String resourceType;

    private String messageCategory;

    private int retryCount;

    private String status;

    private String errorMsg;

    private long costTime;

    protected KafkaCommonTelemetryLog() {
        this.setMetricType(TelemetryConstants.METRIC_NAME);
    }

    public void logInitiateStatus() {
        logWithStatusAndMessage(TelemetryStatus.INITIATE, null);
    }

    public void logSuccessStatus() {
        logWithStatusAndMessage(TelemetryStatus.SUCCESS, null);
    }

    public void logIgnoreStatus(String errorMsg) {
        logWithStatusAndMessage(TelemetryStatus.IGNORE, errorMsg);
    }

    public void logFailureStatus(String errorMsg) {
        logWithStatusAndMessage(TelemetryStatus.FAILURE, errorMsg);
    }

    public void logSuccessStatus(int retryCount) {
        this.retryCount = retryCount;
        logWithStatusAndMessage(TelemetryStatus.SUCCESS, null);
    }

    public void logFailureStatus(String errorMsg, int retryCount) {
        this.retryCount = retryCount;
        logWithStatusAndMessage(TelemetryStatus.FAILURE, errorMsg);
    }

    public void logPendingRetryStatus(String errorMsg, int retryCount) {
        this.retryCount = retryCount;
        logWithStatusAndMessage(TelemetryStatus.PENDING_RETRY, errorMsg);
    }

    private void logWithStatusAndMessage(TelemetryStatus status, String errorMsg) {
        TelemetryLogger telemetryLogger = TelemetryLoggerFactory.getTelemetryLogger();
        this.setStatus(status.getValue());
        this.setErrorMsg(errorMsg);
        telemetryLogger.info(this);
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getMessageVersion() {
        return messageVersion;
    }

    public void setMessageVersion(String messageVersion) {
        this.messageVersion = messageVersion;
    }

    public String getMessageUUID() {
        return messageUUID;
    }

    public void setMessageUUID(String messageUUID) {
        this.messageUUID = messageUUID;
    }

    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getMessageCategory() {
        return messageCategory;
    }

    public void setMessageCategory(String messageCategory) {
        this.messageCategory = messageCategory;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }
}
