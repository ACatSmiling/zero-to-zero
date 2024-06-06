package cn.zero.cloud.platform.kafka.producer.business.event;

import cn.zero.cloud.platform.kafka.common.message.internal.summary.SummaryMessage;
import org.springframework.context.ApplicationEvent;

/**
 * Summary changed事件
 */
public class AbstractSummaryChangeEvent extends ApplicationEvent {
    protected SummaryMessage summaryMessage;

    public AbstractSummaryChangeEvent(Object source,SummaryMessage summaryMessage) {
        super(source);
        this.summaryMessage=summaryMessage;
    }

    public SummaryMessage getSummaryMessage() {
        return summaryMessage;
    }
}
