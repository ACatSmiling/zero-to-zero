package cn.zero.cloud.platform.kafka.consumer.business.processor;

import cn.zero.cloud.platform.kafka.common.message.MessageVersion;
import cn.zero.cloud.platform.kafka.common.message.internal.recording.SuiteRecordingMessage;
import cn.zero.cloud.platform.kafka.common.pojo.result.impl.ConsumeResult;
import cn.zero.cloud.platform.kafka.common.event.KafkaBusinessEvent;
import cn.zero.cloud.platform.kafka.consumer.healthcheck.component.KafkaConsumerHealthCheck;
import cn.zero.cloud.platform.kafka.consumer.processor.KafkaConsumerCommonProcessor;
import cn.zero.cloud.platform.kafka.consumer.business.service.SuiteRecordingMessageService;
import cn.zero.cloud.platform.utils.PlatFormJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * message处理器
 *
 * @author Xisun Wang
 * @since 2024/3/8 16:04
 */
@Slf4j
@Component
public class SuiteRecordingMessageProcessor extends KafkaConsumerCommonProcessor<SuiteRecordingMessage> {
    private final SuiteRecordingMessageService suiteRecordingMessageService;

    @Autowired
    public SuiteRecordingMessageProcessor(SuiteRecordingMessageService suiteRecordingMessageService,
                                          @Qualifier("suiteRecordingNotificationHealthCheck") KafkaConsumerHealthCheck kafkaConsumerHealthCheck) {
        super(kafkaConsumerHealthCheck);
        this.suiteRecordingMessageService = suiteRecordingMessageService;
    }

    @Override
    protected Map<String, ConsumeResult> process(List<SuiteRecordingMessage> messages) {
        Map<String, ConsumeResult> consumeResultMap = new HashMap<>(messages.size());

        // message消费前处理
        List<SuiteRecordingMessage> suiteRecordingMessages = messages.stream()
                .peek(suiteRecordingMessage ->
                        log.info("Consume all suiteRecording message, message body: {}", PlatFormJsonUtil.serializeToJson(suiteRecordingMessage)))
                .filter(suiteRecordingMessage -> (Boolean.TRUE.equals(suiteRecordingMessage.getRecordingMetaData().getEnableAISummary()) &&
                        StringUtils.isNotBlank(suiteRecordingMessage.getRecordingMetaData().getStorageCluster())))
                .toList();

        // message业务消费
        suiteRecordingMessages.forEach(message ->
                consumeResultMap.put(message.getMessageUUID(), suiteRecordingMessageService.consume(message)));
        return consumeResultMap;
    }

    @Override
    protected Class<SuiteRecordingMessage> getMessageClass() {
        return SuiteRecordingMessage.class;
    }

    @Override
    protected MessageVersion getMinSupportedMessageVersion() {
        return MessageVersion.V1;
    }

    @Override
    protected List<KafkaBusinessEvent> getApplicableEvents() {
        return List.of(KafkaBusinessEvent.SUITE_RECORDING_TRANSCRIPT_CREATE_EVENT);
    }
}
