package cn.zero.cloud.platform.kafka.common.constants.type.actiontype;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MeetingSummaryActionType {

    @JsonProperty("SUMMARY_CREATE") SUMMARY_CREATE("SUMMARY_CREATE"),
    @JsonProperty("SUMMARY_UPDATE") SUMMARY_UPDATE("SUMMARY_UPDATE");

    private final String value;

    public String getValue() {
        return value;
    }

    MeetingSummaryActionType(String value) {
        this.value = value;
    }

    public static MeetingSummaryActionType getConcreteActionType(String actionTypeStr) {
        MeetingSummaryActionType[] actionTypes = MeetingSummaryActionType.values();
        for (MeetingSummaryActionType actionType : actionTypes) {
            if (actionType.getValue().equalsIgnoreCase(actionTypeStr)) {
                return actionType;
            }
        }
        return null;
    }
}
