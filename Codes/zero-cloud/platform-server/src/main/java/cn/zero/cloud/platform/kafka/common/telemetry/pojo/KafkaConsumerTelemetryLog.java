package cn.zero.cloud.platform.kafka.common.telemetry.pojo;

import cn.zero.cloud.platform.kafka.common.telemetry.TelemetryConstants;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Xisun Wang
 * @since 2024/3/11 9:22
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KafkaConsumerTelemetryLog extends KafkaCommonTelemetryLog {
    private long partition;

    private long offset;

    private String consumerGroup;

    private String siteUUID;

    private String notificationDestination;

    private long consumerCostTime;

    private long e2EChannelCostTime;

    protected KafkaConsumerTelemetryLog() {
        super();
        this.setVerbType(TelemetryConstants.VERB_CONSUME);
    }

    public KafkaConsumerTelemetryLog(String featureName) {
        this();
        this.setFeatureType(featureName);
    }

    public long getPartition() {
        return partition;
    }

    public void setPartition(long partition) {
        this.partition = partition;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getSiteUUID() {
        return siteUUID;
    }

    public void setSiteUUID(String siteUUID) {
        this.siteUUID = siteUUID;
    }

    public String getNotificationDestination() {
        return notificationDestination;
    }

    public void setNotificationDestination(String notificationDestination) {
        this.notificationDestination = notificationDestination;
    }

    public long getConsumerCostTime() {
        return consumerCostTime;
    }

    public void setConsumerCostTime(long consumerCostTime) {
        this.consumerCostTime = consumerCostTime;
    }

    public long getE2EChannelCostTime() {
        return e2EChannelCostTime;
    }

    public void setE2EChannelCostTime(long e2EChannelCostTime) {
        this.e2EChannelCostTime = e2EChannelCostTime;
    }
}
