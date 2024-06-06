package cn.zero.cloud.platform.kafka.common.message.internal.summary;

import cn.zero.cloud.platform.kafka.common.message.internal.BaseInternalMessage;
import cn.zero.cloud.platform.kafka.common.message.pojo.AuthInfo;
import cn.zero.cloud.platform.kafka.common.constants.type.ResourceType;
import cn.zero.cloud.platform.kafka.common.constants.type.actiontype.MeetingSummaryActionType;
import cn.zero.cloud.platform.utils.PlatFormDateUtil;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Date;
import java.util.UUID;

public class SummaryMessage extends BaseInternalMessage<MeetingSummaryActionType> {
    @NotEmpty(message = "summaryUUID should not be empty!")
    private String summaryUUID;

    private String siteUUID;

    private String version = "2.0";

    private String messageUUID = UUID.randomUUID().toString();

    private long timestamp = System.currentTimeMillis();

    private Date createTime = PlatFormDateUtil.getCurrentDate();

    private String trackingID = StringUtils.isEmpty(MDC.get("trackingID")) ? this.instanceTrackingID() : MDC.get("trackingID").toString();

    private String instanceTrackingID() {
        String trackingSessionID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        this.trackingID = trackingSessionID + "_" + System.currentTimeMillis();
        return trackingSessionID;
    }
    
    @Override
    public ResourceType getResourceType() {
        return ResourceType.SUMMARY;
    }

    @Override
    public String getResourceID() {
        return this.getSummaryUUID();
    }

    @Override
    public String getCurrentSiteUUID() {
        return getSiteUUID();
    }

    @Override
    public MeetingSummaryActionType getActionType() {
        return MeetingSummaryActionType.getConcreteActionType(getActionTypeStr());
    }

    @Override
    public String getProduceRecordKey() {
        return siteUUID + "_" + summaryUUID;
    }

    public @NotEmpty(message = "summaryUUID should not be empty!") String getSummaryUUID() {
        return summaryUUID;
    }

    public void setSummaryUUID(@NotEmpty(message = "summaryUUID should not be empty!") String summaryUUID) {
        this.summaryUUID = summaryUUID;
    }

    public String getSiteUUID() {
        return siteUUID;
    }

    public void setSiteUUID(String siteUUID) {
        this.siteUUID = siteUUID;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getMessageUUID() {
        return messageUUID;
    }

    @Override
    public void setMessageUUID(String messageUUID) {
        this.messageUUID = messageUUID;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getTrackingID() {
        return trackingID;
    }

    @Override
    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
    }
}
