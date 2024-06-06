package cn.zero.cloud.platform.kafka.common.message.internal.recording;

import cn.zero.cloud.platform.kafka.common.message.internal.BaseInternalMessage;
import cn.zero.cloud.platform.kafka.common.message.pojo.OwnerUserInfo;
import cn.zero.cloud.platform.kafka.common.message.pojo.RecordingMetaData;
import cn.zero.cloud.platform.kafka.common.constants.type.ResourceType;
import cn.zero.cloud.platform.kafka.common.constants.ActorInfo;
import cn.zero.cloud.platform.kafka.common.event.KafkaBusinessEvent;
import cn.zero.cloud.platform.kafka.common.constants.type.KafkaEventListType;
import cn.zero.cloud.platform.kafka.common.constants.type.actiontype.RecordingActionType;
import cn.zero.cloud.platform.kafka.common.constants.recording.SuiteRecordingServiceType;
import cn.zero.cloud.platform.kafka.common.message.util.validator.NotNullUnderConditions;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * @author Xisun Wang
 * @since 2024/3/8 16:05
 */
public class SuiteRecordingMessage extends BaseInternalMessage<RecordingActionType> {
    @NotEmpty(message = "OrgID should not be empty!")
    private String orgID;

    @NotNullUnderConditions(
            kafkaEventList = {
                    KafkaBusinessEvent.SUITE_RECORDING_CREATE_EVENT
            },
            listType = KafkaEventListType.INCLUDE,
            message = "CreateTime should not be null!"
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date createTime;

    private SuiteRecordingServiceType serviceType;

    @NotNull(message = "OwnerInfo should not be null!")
    private OwnerUserInfo ownerInfo;

    private ActorInfo actorInfo;

    @NotNull(message = "RecordingMetaData should not be null!")
    private RecordingMetaData recordingMetaData;

    public RecordingMetaData getRecordingMetaData() {
        return recordingMetaData;
    }

    public void setRecordingMetaData(RecordingMetaData recordingMetaData) {
        this.recordingMetaData = recordingMetaData;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SuiteRecordingServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(SuiteRecordingServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public OwnerUserInfo getOwnerInfo() {
        return ownerInfo;
    }

    public void setOwnerInfo(OwnerUserInfo ownerInfo) {
        this.ownerInfo = ownerInfo;
    }

    public ActorInfo getActorInfo() {
        return actorInfo;
    }

    public void setActorInfo(ActorInfo actorInfo) {
        this.actorInfo = actorInfo;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.RECORDING;
    }

    @Override
    public String getResourceID() {
        return recordingMetaData.getRecordingID();
    }

    @Override
    public String getCurrentSiteUUID() {
        return null;
    }

    @Override
    public RecordingActionType getActionType() {
        return RecordingActionType.getConcreteActionType(getActionTypeStr());
    }

    @Override
    public void compatibleWithDeprecatedFields() {
    }

    @Override
    public String getProduceRecordKey() {
        return orgID + "_" + recordingMetaData.getRecordingID();
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }
}
