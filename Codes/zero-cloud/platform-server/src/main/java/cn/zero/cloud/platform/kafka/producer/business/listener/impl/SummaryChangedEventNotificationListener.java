package cn.zero.cloud.platform.kafka.producer.business.listener.impl;

import cn.zero.cloud.platform.kafka.common.message.internal.summary.SummaryMessage;
import cn.zero.cloud.platform.kafka.producer.business.service.SummaryChangedEventNotificationService;
import cn.zero.cloud.platform.kafka.producer.business.event.AbstractSummaryChangeEvent;
import cn.zero.cloud.platform.kafka.producer.business.listener.AbstractEventListener;
import cn.zero.cloud.platform.utils.PlatFormJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Summary changed事件通知监听
 */
@Slf4j
@Component
public class SummaryChangedEventNotificationListener extends AbstractEventListener<AbstractSummaryChangeEvent> {
    private final SummaryChangedEventNotificationService summaryChangedEventNotificationService;

    public SummaryChangedEventNotificationListener(@Autowired SummaryChangedEventNotificationService summaryChangedEventNotificationService) {
        this.summaryChangedEventNotificationService = summaryChangedEventNotificationService;
    }

    @Async(value = "SendNotificationExecutor")
    @Override
    public void onApplicationEvent(@NonNull AbstractSummaryChangeEvent event) {
        super.onApplicationEvent(event);
    }

    @Override
    protected boolean isIgnoredEvent(AbstractSummaryChangeEvent event) {
        // 当前事件全部正常执行
        return false;
    }

    @Override
    protected void onEvent(AbstractSummaryChangeEvent event) {
        SummaryMessage message = event.getSummaryMessage();
        log.info("Listener summary changed message event, detail: {}", PlatFormJsonUtil.serializeToJson(message));

        summaryChangedEventNotificationService.send(message);
    }
}
