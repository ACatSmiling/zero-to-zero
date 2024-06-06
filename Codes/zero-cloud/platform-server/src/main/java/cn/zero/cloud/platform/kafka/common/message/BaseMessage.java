package cn.zero.cloud.platform.kafka.common.message;

import cn.zero.cloud.platform.kafka.common.message.pojo.MetaData;
import cn.zero.cloud.platform.kafka.common.message.pojo.MetaDataBuilder;
import cn.zero.cloud.platform.kafka.common.constants.type.ResourceType;
import cn.zero.cloud.platform.kafka.common.exception.KafkaMessageParseException;
import cn.zero.cloud.platform.kafka.common.exception.KafkaMessageValidationException;
import cn.zero.cloud.platform.kafka.common.constants.MessageCategory;
import cn.zero.cloud.platform.kafka.common.constants.OperationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.kafka.common.header.Headers;
import org.slf4j.MDC;

import java.util.UUID;

import static cn.zero.cloud.platform.kafka.common.constants.KafkaCommonConstants.TRACKING_ID;

/**
 * @author Xisun Wang
 * @since 2024/3/6 14:37
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseMessage<A> {
    @JsonIgnore
    private String recordTopic;

    @JsonIgnore
    private String recordKey;

    @JsonIgnore
    private Object recordValue;

    @JsonIgnore
    private Headers recordHeaders;

    @NotEmpty(message = "Version should not be empty!")
    private String version;

    @NotEmpty(message = "MessageUUID should not be empty!")
    private String messageUUID;

    @NotEmpty(message = "TrackingID should not be empty!")
    private String trackingID;

    @NotNull(message = "OperationType should not be null!")
    private OperationType operationType;

    private String actionType;

    private String url;

    private MetaData metaData;

    @Min(value = 1, message = "Timestamp should not be empty!")
    private long timestamp;

    public BaseMessage() {
        this.setVersion("1.0");
        this.setMessageUUID(UUID.randomUUID().toString());
        this.setTrackingID((StringUtils.isEmpty(MDC.get(TRACKING_ID))
                ? instanceTrackingID()
                : MDC.get(TRACKING_ID)));
        this.setMetaData(new MetaDataBuilder().build());
        this.setTimestamp(System.currentTimeMillis());
    }

    /**
     * actionType由子类定义
     *
     * @return actionType
     */
    public abstract A getActionType();

    public abstract String getResourceID();

    @JsonIgnore
    public abstract ResourceType getResourceType();

    @JsonIgnore
    public abstract String getCurrentSiteUUID();

    /**
     * 消息类别，子类消息需要实现
     *
     * @return 消息类别
     */
    @JsonIgnore
    public abstract MessageCategory getMessageCategory();

    @JsonIgnore
    public abstract String getProduceRecordKey();

    private String instanceTrackingID() {
        String trackingSessionID = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return trackingSessionID + "_" + System.currentTimeMillis();
    }

    /**
     * 验证action type
     *
     * @throws KafkaMessageValidationException 验证失败，抛出异常
     */
    public final void validateActionType() throws KafkaMessageValidationException {
        String actionTypeStr = getActionTypeStr();

        if (getActionType() == null) {
            throw new KafkaMessageValidationException("Invalid actionType value! Message = " + this.messageUUID + ", Value = " + actionTypeStr + ".");
        }

        if (getOperationType() == OperationType.ACTION) {
            if (StringUtils.isBlank(actionTypeStr)) {
                throw new KafkaMessageValidationException("The actionType of [" + this.messageUUID + "] should not be empty when operationType is [ACTION]!");
            }
        } else {
            if (StringUtils.isNotBlank(actionTypeStr)) {
                throw new KafkaMessageValidationException("The actionType of [" + this.messageUUID + "] is only available when operationType is [ACTION]!");
            }
        }
    }

    /**
     * 版本兼容性处理
     */
    public void compatibleWithDeprecatedFields() {
        // Do nothing if there're no deprecated fields in the legacy versions which need to keep compatibility
    }

    /**
     * 外部消息必填字段处理
     *
     * @throws KafkaMessageParseException KafkaMessageParseException
     */
    public void handleRequiredFields() throws KafkaMessageParseException {
        // Handle required fields for external message.
    }

    /**
     * 获取message的version
     *
     * @return message version
     */
    public double messageVersion() {
        return Double.parseDouble(StringUtils.trim(getVersion()));
    }

    public String getRecordTopic() {
        return recordTopic;
    }

    public void setRecordTopic(String recordTopic) {
        this.recordTopic = recordTopic;
    }

    public String getRecordKey() {
        return recordKey;
    }

    public void setRecordKey(String recordKey) {
        this.recordKey = recordKey;
    }

    public Object getRecordValue() {
        return recordValue;
    }

    public void setRecordValue(Object recordValue) {
        this.recordValue = recordValue;
    }

    public Headers getRecordHeaders() {
        return recordHeaders;
    }

    public void setRecordHeaders(Headers recordHeaders) {
        this.recordHeaders = recordHeaders;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    @JsonIgnore
    public String getActionTypeStr() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timeStamp) {
        if (timeStamp < 0) {
            this.timestamp = System.currentTimeMillis();
        } else {
            this.timestamp = timeStamp;
        }
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
