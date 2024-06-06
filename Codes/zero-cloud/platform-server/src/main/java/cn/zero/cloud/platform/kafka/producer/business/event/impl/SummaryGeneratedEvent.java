package cn.zero.cloud.platform.kafka.producer.business.event.impl;

import cn.zero.cloud.platform.kafka.producer.business.event.AbstractSummaryChangeEvent;
import cn.zero.cloud.platform.kafka.common.message.internal.summary.SummaryMessage;
import cn.zero.cloud.platform.kafka.common.constants.OperationType;

/**
 * Summary生成事件
 */
public class SummaryGeneratedEvent extends AbstractSummaryChangeEvent {
    public SummaryGeneratedEvent(Object source, SummaryMessage summaryMessage) {
        super(source, summaryMessage);
        summaryMessage.setOperationType(OperationType.CREATE);
    }
}
