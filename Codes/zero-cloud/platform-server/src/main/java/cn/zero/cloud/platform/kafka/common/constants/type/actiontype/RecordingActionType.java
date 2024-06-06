package cn.zero.cloud.platform.kafka.common.constants.type.actiontype;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Xisun Wang
 * @since 2024/3/8 16:23
 */
public enum RecordingActionType {
    @JsonProperty("SHARE") SHARE("SHARE"),

    @JsonProperty("TRASH") TRASH("TRASH"),

    @JsonProperty("RESTORE") RESTORE("RESTORE"),

    @JsonProperty("PURGE") PURGE("PURGE"),

    @JsonProperty("REASSIGN") REASSIGN("REASSIGN"),

    @JsonProperty("DOWNLOAD") DOWNLOAD("DOWNLOAD"),

    @JsonProperty("PLAYBACK") PLAYBACK("PLAYBACK"),

    @JsonProperty("TRANSCRIPT_CREATE") TRANSCRIPT_CREATE("TRANSCRIPT_CREATE"),

    @JsonProperty("TRANSCRIPT_UPDATE") TRANSCRIPT_UPDATE("TRANSCRIPT_UPDATE");

    private final String value;

    RecordingActionType(String value) {
        this.value = value;
    }

    public static RecordingActionType getConcreteActionType(String actionTypeStr) {
        RecordingActionType[] actionTypes = RecordingActionType.values();
        for (RecordingActionType actionType : actionTypes) {
            if (actionType.getValue().equalsIgnoreCase(actionTypeStr)) {
                return actionType;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}