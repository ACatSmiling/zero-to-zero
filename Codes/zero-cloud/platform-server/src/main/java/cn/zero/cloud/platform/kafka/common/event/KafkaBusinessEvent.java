package cn.zero.cloud.platform.kafka.common.event;

import cn.zero.cloud.platform.kafka.common.constants.OperationType;
import cn.zero.cloud.platform.kafka.common.message.BaseMessage;
import cn.zero.cloud.platform.kafka.common.message.internal.recording.SuiteRecordingMessage;
import cn.zero.cloud.platform.kafka.common.message.internal.summary.SummaryMessage;
import cn.zero.cloud.platform.kafka.common.message.internal.summary.SummaryTranscriptMessage;
import cn.zero.cloud.platform.kafka.common.message.internal.transcript.TranscriptMessage;
import org.apache.commons.lang3.StringUtils;

import static cn.zero.cloud.platform.kafka.common.constants.OperationType.*;
import static cn.zero.cloud.platform.kafka.common.constants.type.actiontype.RecordingActionType.*;
import static cn.zero.cloud.platform.kafka.common.constants.type.actiontype.TranscriptActionType.*;

/**
 * @author Xisun Wang
 * @since 2024/3/8 16:21
 */
public enum KafkaBusinessEvent {
    /**
     * 默认event，全部适用
     */
    ALL_EVENTS(null, null, null, true),

    /**
     * SuiteRecording Business Events
     */
    SUITE_RECORDING_CREATE_EVENT(SuiteRecordingMessage.class, CREATE, null, false),
    SUITE_RECORDING_DELETE_EVENT(SuiteRecordingMessage.class, DELETE, null, false),
    SUITE_RECORDING_TRASH_EVENT(SuiteRecordingMessage.class, ACTION, TRASH.getValue(), false),
    SUITE_RECORDING_PURGE_EVENT(SuiteRecordingMessage.class, ACTION, PURGE.getValue(), false),
    SUITE_RECORDING_TRANSCRIPT_CREATE_EVENT(SuiteRecordingMessage.class, ACTION, TRANSCRIPT_CREATE.getValue(), false),
    SUITE_RECORDING_RESTORE_EVENT(SuiteRecordingMessage.class, ACTION, RESTORE.getValue(), false),

    /**
     * Transcript Business Events
     */
    TRANSCRIPT_CREATE_EVENT(TranscriptMessage.class, CREATE, null, false),
    TRANSCRIPT_INITIALIZE_EVENT(TranscriptMessage.class, ACTION, INITIALIZE.getValue(), false),
    TRANSCRIPT_UPDATE_SNIPPET_EVENT(TranscriptMessage.class, ACTION, UPDATE_SNIPPET.getValue(), false),
    TRANSCRIPT_PURGE_EVENT(TranscriptMessage.class, ACTION, PURGE_TRANSCRIPT.getValue(), false),
    TRANSCRIPT_DELETE_EVENT(TranscriptMessage.class, DELETE, null, false),


    /**
     * Meeting Summary Events
     */
    SUMMARY_CREATE_EVENT(SummaryMessage.class, CREATE, null, false),

    SUMMARY_TRANSCRIPT_CREATE_EVENT(SummaryTranscriptMessage.class, CREATE, null, false);

    /**
     * 消息类型
     */
    private final Class<? extends BaseMessage> messageType;

    /**
     * 操作类型
     */
    private final OperationType operationType;

    /**
     * 动作类型
     */
    private final String actionType;

    /**
     * 是否适用全部event
     */
    private final boolean isMatchAllEvents;

    KafkaBusinessEvent(Class<? extends BaseMessage> messageType, OperationType operationType, String actionType, boolean isMatchAllEvents) {
        this.messageType = messageType;
        this.operationType = operationType;
        this.actionType = actionType;
        this.isMatchAllEvents = isMatchAllEvents;
    }

    /**
     * 验证message是否适用 current event
     *
     * @param message message
     * @return 是否适用event
     */
    public boolean matchThisEvent(BaseMessage message) {
        if (message == null) {
            return false;
        }
        return matchThisEvent(message.getClass().getCanonicalName(), message.getOperationType(), message.getActionTypeStr());
    }

    /**
     * 验证message是否适用 current event
     *
     * @param messageClassName 消息类型
     * @param operationType    操作类型
     * @param actionType       动作类型
     * @return 是否适用event
     */
    public boolean matchThisEvent(String messageClassName, OperationType operationType, String actionType) {
        if (this.isMatchAllEvents) {
            return true;
        }

        if (!StringUtils.equals(this.messageType.getCanonicalName(), messageClassName)) {
            return false;
        }

        if (this.operationType != operationType) {
            return false;
        }

        if (operationType == ACTION) {
            return StringUtils.equalsIgnoreCase(this.actionType, actionType);
        }
        return true;
    }

    /**
     * 获取message适用的event
     *
     * @param message message
     * @return message适用的event
     */
    public static KafkaBusinessEvent getKafkaBusinessEvent(BaseMessage message) {
        if (message == null) {
            throw new IllegalArgumentException("Event match for Message not found!");
        }
        return getKafkaBusinessEvent(message.getClass().getCanonicalName(), message.getOperationType(), message.getActionTypeStr());
    }

    /**
     * 获取message适用的event
     *
     * @param messageClassName 消息类型
     * @param operationType    操作类型
     * @param actionType       动作类型
     * @return message适用的event
     */
    public static KafkaBusinessEvent getKafkaBusinessEvent(String messageClassName, OperationType operationType, String actionType) {
        KafkaBusinessEvent[] events = KafkaBusinessEvent.values();
        for (KafkaBusinessEvent kafkaBusinessEvent : events) {
            if (kafkaBusinessEvent == ALL_EVENTS) {
                continue;
            }
            if (kafkaBusinessEvent.matchThisEvent(messageClassName, operationType, actionType)) {
                return kafkaBusinessEvent;
            }
        }
        throw new IllegalArgumentException("Event match for Message [" + messageClassName + "] not found!");
    }
}
