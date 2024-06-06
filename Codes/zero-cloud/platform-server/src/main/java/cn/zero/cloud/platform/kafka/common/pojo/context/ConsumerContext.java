package cn.zero.cloud.platform.kafka.common.pojo.context;

import cn.zero.cloud.platform.kafka.common.pojo.result.impl.ConsumeResult;

/**
 * @author Xisun Wang
 * @since 2024/3/8 17:29
 */
public class ConsumerContext {
    private String topic;

    private int partition;

    private long offset;

    private Object key;

    private String value;

    private String consumerGroup;

    private String messageVersion;

    private String messageUUID;

    private String trackingID;

    private String operationType;

    private String actionType;

    private String resourceID;

    private String resourceType;

    private String siteUUID;

    private String messageCategory;

    private int retryCount;

    private ConsumeResult consumeResult;

    private long initStartTime;

    private long initCostTime;

    private ConsumerContext() {
    }

    public static ConsumerContext createConsumerContext() {
        return new ConsumerContext();
    }

    public void startInit() {
        this.initStartTime = System.currentTimeMillis();
    }

    public void endInit() {
        long initEndTime = System.currentTimeMillis();
        this.initCostTime = initEndTime - initStartTime;
    }

    public long getConsumerCostTime() {
        long processCostTime = (consumeResult == null) ? 0L : consumeResult.getConsumeCostTime();
        return initCostTime + processCostTime;
    }

    public long getE2EChannelCostTime() {
        return (consumeResult == null) ? -1L : consumeResult.getE2EChannelCostTime();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
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

    public String getSiteUUID() {
        return siteUUID;
    }

    public void setSiteUUID(String siteUUID) {
        this.siteUUID = siteUUID;
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

    public ConsumeResult getConsumeResult() {
        return consumeResult;
    }

    public void setConsumeResult(ConsumeResult consumeResult) {
        this.consumeResult = consumeResult;
    }

    public long getInitCostTime() {
        return initCostTime;
    }
}
