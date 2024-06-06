package cn.zero.cloud.platform.service.impl;

import cn.zero.cloud.platform.kafka.common.message.internal.summary.SummaryMessage;
import cn.zero.cloud.platform.kafka.producer.business.event.impl.SummaryGeneratedEvent;
import cn.zero.cloud.platform.service.SummaryApiService;
import cn.zero.cloud.platform.utils.PlatFormJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Xisun Wang
 * @since 2024/4/12 16:21
 */
@Slf4j
@Service
public class SummaryApiServiceImpl implements SummaryApiService {
    private ApplicationContext context;

    @Autowired
    public SummaryApiServiceImpl(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void sendSummaryMessage() {
        SummaryMessage message = new SummaryMessage();
        message.setSummaryUUID(UUID.randomUUID().toString());
        SummaryGeneratedEvent summaryGeneratedEvent = new SummaryGeneratedEvent(this, message);
        log.info("message: {}", PlatFormJsonUtil.serializeToJson(message));
        context.publishEvent(summaryGeneratedEvent);
    }
}
