package cn.zero.cloud.platform.kafka.common.message.internal.summary;

import cn.zero.cloud.platform.kafka.common.message.internal.BaseInternalMessage;
import cn.zero.cloud.platform.kafka.common.message.pojo.AuthInfo;
import cn.zero.cloud.platform.kafka.common.constants.type.ResourceType;
import cn.zero.cloud.platform.kafka.common.constants.type.actiontype.MeetingSummaryTranscriptActionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class SummaryTranscriptMessage extends BaseInternalMessage<MeetingSummaryTranscriptActionType> {
    @NotEmpty(message = "SiteUUID should not be empty!")
    private String siteUUID;

    private String ciOrgID;

    @NotEmpty(message = "transcriptUUID should not be empty!")
    private String transcriptUUID;

    @NotEmpty(message = "transcriptUrl should not be empty!")
    private String transcriptUrl;

    @NotNull(message = "ParentType should not be null!")
    private ResourceType parentType;

    @NotEmpty(message = "ParentID should not be empty!")
    private String parentID;

    private String aclUrl;
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

    public String getTranscriptUUID() {
        return transcriptUUID;
    }

    public void setTranscriptUUID(String transcriptUUID) {
        this.transcriptUUID = transcriptUUID;
    }

    public String getTranscriptUrl() {
        return transcriptUrl;
    }

    public void setTranscriptUrl(String transcriptUrl) {
        this.transcriptUrl = transcriptUrl;
    }

    public String getAclUrl() {
        return aclUrl;
    }

    public void setAclUrl(String aclUrl) {
        this.aclUrl = aclUrl;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.SUMMARY_TRANSCRIPT;
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
    public MeetingSummaryTranscriptActionType getActionType() {
        return MeetingSummaryTranscriptActionType.getConcreteActionType(getActionTypeStr());
    }

    @Override
    public String getProduceRecordKey() {
        return siteUUID + "_" + transcriptUUID;
    }
}
