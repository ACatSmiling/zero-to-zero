package cn.zero.cloud.platform.kafka.producer.business.event.impl;

import cn.zero.cloud.platform.kafka.producer.business.event.AbstractTranscriptChangedEvent;
import cn.zero.cloud.platform.kafka.common.message.internal.transcript.TranscriptMessage;
import cn.zero.cloud.platform.kafka.common.constants.OperationType;

public class TranscriptDeleteEvent extends AbstractTranscriptChangedEvent {
    public TranscriptDeleteEvent(Object source, TranscriptMessage transcriptMessage) {
        super(source, transcriptMessage);
        transcriptMessage.setOperationType(OperationType.DELETE);
    }
}
