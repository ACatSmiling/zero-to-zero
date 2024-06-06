package cn.zero.cloud.platform.kafka.producer.business.event.impl;

import cn.zero.cloud.platform.kafka.producer.business.event.AbstractSummaryTranscriptChangeEvent;
import cn.zero.cloud.platform.kafka.common.message.internal.summary.SummaryTranscriptMessage;
import cn.zero.cloud.platform.kafka.common.constants.OperationType;

public class SummaryTranscriptInitializeEvent extends AbstractSummaryTranscriptChangeEvent {
    public SummaryTranscriptInitializeEvent(Object source, SummaryTranscriptMessage summaryTranscriptMessage) {
        super(source, summaryTranscriptMessage);
        summaryTranscriptMessage.setOperationType(OperationType.CREATE);
    }
}
