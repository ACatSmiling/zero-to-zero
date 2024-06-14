package cn.zero.cloud.platform.kafka.consumer.business.listener;

import cn.zero.cloud.platform.kafka.consumer.business.processor.SuiteRecordingMessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * message监听器
 *
 * @author Xisun Wang
 * @since 2024/3/6 14:25
 */
@Slf4j
@Component
public class SuiteRecordingMessageListener {
    @Value("${zero.cloud.kafka.notification.message.suite-recording.ai-integration-group:ai_integration_group_6}")
    private String AI_INTEGRATION_GROUP;

    private final SuiteRecordingMessageProcessor suiteRecordingMessageProcessor;

    public SuiteRecordingMessageListener(@Autowired SuiteRecordingMessageProcessor suiteRecordingMessageProcessor) {
        this.suiteRecordingMessageProcessor = suiteRecordingMessageProcessor;
    }

    @KafkaListener(
            autoStartup = "${zero.cloud.kafka.notification.message.suite-recording.enable:false}",
            topics = {
                    "#{'${zero.cloud.kafka.notification.message.suite-recording.topic}'.split(',')}"
            },
            groupId = "${zero.cloud.kafka.notification.message.suite-recording.ai-integration-group:ai_integration_group_6}",
            containerFactory = "rateLimitConcurrentKafkaListenerContainerFactory"
    )
    public void listen(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        suiteRecordingMessageProcessor.process(records, ack, AI_INTEGRATION_GROUP);
    }
}
