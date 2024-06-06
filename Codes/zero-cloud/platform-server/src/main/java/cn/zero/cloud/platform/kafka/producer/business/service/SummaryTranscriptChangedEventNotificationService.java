package cn.zero.cloud.platform.kafka.producer.business.service;

import cn.zero.cloud.platform.kafka.common.message.internal.summary.SummaryTranscriptMessage;
import cn.zero.cloud.platform.kafka.producer.notification.KafkaNotificationSender;
import cn.zero.cloud.platform.kafka.common.pojo.notification.KafkaNotificationTopics;
import cn.zero.cloud.platform.utils.PlatFormJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * SummaryTranscriptNotificationService，Summary transcript changed event通知处理业务
 */
@Slf4j
@Component
public class SummaryTranscriptChangedEventNotificationService {
    private final KafkaNotificationTopics kafkaNotificationTopics;

    private final KafkaNotificationSender kafkaNotificationSender;


    public SummaryTranscriptChangedEventNotificationService(@Autowired KafkaNotificationTopics kafkaNotificationTopics,
                                                            @Autowired KafkaNotificationSender kafkaNotificationSender) {
        this.kafkaNotificationTopics = kafkaNotificationTopics;
        this.kafkaNotificationSender = kafkaNotificationSender;
    }

    public void send(SummaryTranscriptMessage summaryTranscriptMessage) {
        try {
            String topic = kafkaNotificationTopics.getSummaryTranscriptTopic();
            log.trace("Send summary transcript changed message, summaryTranscriptMessage: {}, topic:{}",
                    PlatFormJsonUtil.serializeToJson(summaryTranscriptMessage), topic);
            kafkaNotificationSender.sendMessage(summaryTranscriptMessage, topic);
        } catch (Exception e) {
            log.error("Send summary transcript changed message fail, messageUUID: {}, exception: ", summaryTranscriptMessage.getMessageUUID(), e);
        }
    }
}
