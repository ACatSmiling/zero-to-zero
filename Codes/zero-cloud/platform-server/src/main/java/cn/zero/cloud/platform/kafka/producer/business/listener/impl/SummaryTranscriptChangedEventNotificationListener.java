package cn.zero.cloud.platform.kafka.producer.business.listener.impl;

import cn.zero.cloud.platform.kafka.producer.business.service.SummaryTranscriptChangedEventNotificationService;
import cn.zero.cloud.platform.kafka.producer.business.event.AbstractSummaryTranscriptChangeEvent;
import cn.zero.cloud.platform.kafka.producer.business.listener.AbstractEventListener;
import cn.zero.cloud.platform.kafka.common.message.internal.summary.SummaryTranscriptChangedEventMessage;
import cn.zero.cloud.platform.utils.PlatFormJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * SummaryTranscript changed事件通知监听
 */
@Slf4j
@Component
public class SummaryTranscriptChangedEventNotificationListener extends AbstractEventListener<AbstractSummaryTranscriptChangeEvent> {
    private final SummaryTranscriptChangedEventNotificationService summaryTranscriptChangedEventNotificationService;

    public SummaryTranscriptChangedEventNotificationListener(@Autowired SummaryTranscriptChangedEventNotificationService summaryTranscriptChangedEventNotificationService) {
        this.summaryTranscriptChangedEventNotificationService = summaryTranscriptChangedEventNotificationService;
    }

    @Async(value = "SendNotificationExecutor")
    @Override
    public void onApplicationEvent(@NonNull AbstractSummaryTranscriptChangeEvent event) {
        super.onApplicationEvent(event);
    }

    @Override
    protected boolean isIgnoredEvent(AbstractSummaryTranscriptChangeEvent event) {
        // 当前事件全部正常执行
        return false;
    }

    @Override
    protected void onEvent(AbstractSummaryTranscriptChangeEvent event) {
        SummaryTranscriptChangedEventMessage message = (SummaryTranscriptChangedEventMessage) event.getSummaryTranscriptMessage();
        log.info("Listener summary transcript changed message event, detail: {}", PlatFormJsonUtil.serializeToJson(message));

        summaryTranscriptChangedEventNotificationService.send(message);
    }
}
