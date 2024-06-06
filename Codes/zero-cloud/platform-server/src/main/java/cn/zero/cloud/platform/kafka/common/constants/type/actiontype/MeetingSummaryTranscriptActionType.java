package cn.zero.cloud.platform.kafka.common.constants.type.actiontype;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MeetingSummaryTranscriptActionType {

    @JsonProperty("SUMMARY_TRANSCRIPT_CREATE") SUMMARY_TRANSCRIPT_CREATE("SUMMARY_TRANSCRIPT_CREATE");

    private final String value;

    public String getValue() {
        return value;
    }

    MeetingSummaryTranscriptActionType(String value) {
        this.value = value;
    }

    public static MeetingSummaryTranscriptActionType getConcreteActionType(String actionTypeStr) {
        MeetingSummaryTranscriptActionType[] actionTypes = MeetingSummaryTranscriptActionType.values();
        for (MeetingSummaryTranscriptActionType actionType : actionTypes) {
            if (actionType.getValue().equalsIgnoreCase(actionTypeStr)) {
                return actionType;
            }
        }
        return null;
    }
}
