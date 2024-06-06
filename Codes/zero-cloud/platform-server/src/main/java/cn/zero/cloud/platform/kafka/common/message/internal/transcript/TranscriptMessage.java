package cn.zero.cloud.platform.kafka.common.message.internal.transcript;

import cn.zero.cloud.platform.kafka.common.message.internal.BaseInternalMessage;
import cn.zero.cloud.platform.kafka.common.message.pojo.AuthInfo;
import cn.zero.cloud.platform.kafka.common.constants.type.ResourceType;
import cn.zero.cloud.platform.kafka.common.constants.ActorInfo;
import cn.zero.cloud.platform.kafka.common.event.KafkaBusinessEvent;
import cn.zero.cloud.platform.kafka.common.constants.type.KafkaEventListType;
import cn.zero.cloud.platform.kafka.common.constants.type.actiontype.TranscriptActionType;
import cn.zero.cloud.platform.kafka.common.message.util.validator.NotEmptyUnderConditions;
import cn.zero.cloud.platform.kafka.common.message.util.validator.NotNullUnderConditions;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class TranscriptMessage extends BaseInternalMessage<TranscriptActionType> {
    @NotEmpty(message = "SiteUUID should not be empty!")
    private String siteUUID;

    /**
     * Added in v2.0
     * See Details: https://wiki.cisco.com/x/lrTUHw
     */
    private String ciOrgID;

    @NotNull(message = "ParentType should not be null!")
    private ResourceType parentType;

    @NotEmpty(message = "ParentID should not be empty!")
    private String parentID;

    @NotEmpty(message = "TranscriptUUID should not be empty!")
    private String transcriptUUID;

    @NotEmptyUnderConditions(
            kafkaEventList = {
                    KafkaBusinessEvent.TRANSCRIPT_INITIALIZE_EVENT
            },
            listType = KafkaEventListType.INCLUDE,
            message = "HighlightUUID should not be empty!"
    )
    private String highlightUUID;

    @NotEmptyUnderConditions(
            kafkaEventList = {
                    KafkaBusinessEvent.TRANSCRIPT_INITIALIZE_EVENT
            },
            listType = KafkaEventListType.INCLUDE,
            message = "AudioUUID should not be empty!"
    )
    private String audioUUID;

    @NotNullUnderConditions(
            kafkaEventList = {
                    KafkaBusinessEvent.TRANSCRIPT_UPDATE_SNIPPET_EVENT
            },
            listType = KafkaEventListType.INCLUDE,
            message = "SnippetUUIDs should not be null!"
    )
    private List<String> snippetUUIDs;

    private ActorInfo actorInfo;


    // new added for use ccp kms to encrypt if exist
    private String keyUrl;
    private String kroUrl;
    private AuthInfo authInfo;

    public String getSiteUUID() {
        return siteUUID;
    }

    public void setSiteUUID(String siteUUID) {
        this.siteUUID = siteUUID;
    }

    public String getCiOrgID() {
        return ciOrgID;
    }

    public void setCiOrgID(String ciOrgID) {
        this.ciOrgID = ciOrgID;
    }

    public ResourceType getParentType() {
        return parentType;
    }

    public void setParentType(ResourceType parentType) {
        this.parentType = parentType;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getTranscriptUUID() {
        return transcriptUUID;
    }

    public void setTranscriptUUID(String transcriptUUID) {
        this.transcriptUUID = transcriptUUID;
    }

    public String getHighlightUUID() {
        return highlightUUID;
    }

    public void setHighlightUUID(String highlightUUID) {
        this.highlightUUID = highlightUUID;
    }

    public String getAudioUUID() {
        return audioUUID;
    }

    public void setAudioUUID(String audioUUID) {
        this.audioUUID = audioUUID;
    }

    public List<String> getSnippetUUIDs() {
        return snippetUUIDs;
    }

    public void setSnippetUUIDs(List<String> snippetUUIDs) {
        this.snippetUUIDs = snippetUUIDs;
    }

    public ActorInfo getActorInfo() {
        return actorInfo;
    }

    public void setActorInfo(ActorInfo actorInfo) {
        this.actorInfo = actorInfo;
    }

    public String getKeyUrl() {
        return keyUrl;
    }

    public void setKeyUrl(String keyUrl) {
        this.keyUrl = keyUrl;
    }

    public String getKroUrl() {
        return kroUrl;
    }

    public void setKroUrl(String kroUrl) {
        this.kroUrl = kroUrl;
    }

    public AuthInfo getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(AuthInfo authInfo) {
        this.authInfo = authInfo;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.TRANSCRIPT;
    }

    @Override
    public String getResourceID() {
        return this.getTranscriptUUID();
    }

    @Override
    public String getCurrentSiteUUID() {
        return getSiteUUID();
    }

    @Override
    public TranscriptActionType getActionType() {
        return TranscriptActionType.getConcreteActionType(getActionTypeStr());
    }

    @Override
    public String getProduceRecordKey() {
        return siteUUID + "_" + transcriptUUID;
    }

}
