package cn.zero.cloud.platform.kafka.common.constants.type.actiontype;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TranscriptActionType {
    @JsonProperty("INITIALIZE") INITIALIZE("INITIALIZE"),
    @JsonProperty("UPDATE_SNIPPET") UPDATE_SNIPPET("UPDATE_SNIPPET"),
    @JsonProperty("PURGE") PURGE_TRANSCRIPT("PURGE");

    private final String value;

    public String getValue() {
        return value;
    }

    TranscriptActionType(String value) {
        this.value = value;
    }

    public static TranscriptActionType getConcreteActionType(String actionTypeStr) {
        TranscriptActionType[] actionTypes = TranscriptActionType.values();
        for (TranscriptActionType actionType : actionTypes) {
            if (actionType.getValue().equalsIgnoreCase(actionTypeStr)) {
                return actionType;
            }
        }
        return null;
    }
}
