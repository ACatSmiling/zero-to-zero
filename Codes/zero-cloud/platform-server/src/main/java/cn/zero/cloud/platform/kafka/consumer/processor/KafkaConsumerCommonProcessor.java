package cn.zero.cloud.platform.kafka.consumer.processor;

import cn.zero.cloud.platform.kafka.common.message.util.parser.MessageParser;
import cn.zero.cloud.platform.kafka.common.message.util.validator.MessageValidator;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorSummary;
import cn.zero.cloud.platform.kafka.common.message.BaseMessage;
import cn.zero.cloud.platform.kafka.common.message.MessageVersion;
import cn.zero.cloud.platform.kafka.common.pojo.result.impl.ConsumeResult;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorCode;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorDetail;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorType;
import cn.zero.cloud.platform.kafka.common.exception.KafkaMessageParseException;
import cn.zero.cloud.platform.kafka.common.exception.KafkaMessageValidationException;
import cn.zero.cloud.platform.kafka.common.event.KafkaBusinessEvent;
import cn.zero.cloud.platform.kafka.utils.KafkaCommonUtil;
import cn.zero.cloud.platform.kafka.consumer.healthcheck.component.KafkaConsumerHealthCheck;
import cn.zero.cloud.platform.kafka.common.pojo.context.ConsumerContext;
import cn.zero.cloud.platform.kafka.common.telemetry.factory.KafkaConsumerTelemetryLogFactory;
import cn.zero.cloud.platform.kafka.common.telemetry.pojo.KafkaConsumerTelemetryLog;
import cn.zero.cloud.platform.utils.PlatFormJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author Xisun Wang
 * @since 2024/3/6 14:36
 */
@Component
public abstract class KafkaConsumerCommonProcessor<T extends BaseMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerCommonProcessor.class);

    @Autowired
    private MessageParser<T> messageParser;

    @Autowired
    private MessageValidator<T> messageValidator;

    private final KafkaConsumerHealthCheck kafkaConsumerHealthCheck;

    public KafkaConsumerCommonProcessor(KafkaConsumerHealthCheck kafkaConsumerHealthCheck) {
        this.kafkaConsumerHealthCheck = kafkaConsumerHealthCheck;
    }

    /**
     * 当前消费的record的最小支持版本，子类需实现
     *
     * @return 版本号
     */
    protected abstract MessageVersion getMinSupportedMessageVersion();

    /**
     * 获取消息的类型，子类需实现
     *
     * @return 消息的class
     */
    protected abstract Class<T> getMessageClass();

    /**
     * 处理消息，子类需实现
     *
     * @param messages 消息集合
     * @return 消息处理结果集合，key为messageUUID
     */
    protected abstract Map<String, ConsumeResult> process(List<T> messages);

    /**
     * record处理
     *
     * @param records         待处理的record集合
     * @param ack             ack
     * @param consumerGroupID 消费者组ID
     */
    public final void process(List<ConsumerRecord<?, ?>> records, Acknowledgment ack, String consumerGroupID) {
        if (!meetPreCondition(records, ack)) {
            return;
        }

        List<T> messages = new ArrayList<>(records.size());
        Map<String, ConsumerContext> consumerContextMap = new LinkedHashMap<>(records.size());

        initMessage(records, consumerGroupID, messages, consumerContextMap);

        processMessage(messages, consumerContextMap, ack);

        logConsumeResult(consumerContextMap);
    }

    /**
     * record处理的先决条件判断
     *
     * @param records 待处理的record记录
     * @param ack     ack
     * @return 是否满足消息处理的先决条件
     */
    private boolean meetPreCondition(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        if (kafkaConsumerHealthCheck.isNotificationDisable()) {
            // 未开启消息通知时，手动确认消息消费，并return
            if (ack != null) {
                ack.acknowledge();
            }
            return false;
        }

        if (records == null || records.isEmpty()) {
            // 待处理的消息为null或空时，手动确认消息消费，并return
            if (ack != null) {
                ack.acknowledge();
            }
            return false;
        }

        return true;
    }

    /**
     * 初始化record为message
     *
     * @param records            consumer record
     * @param consumerGroupID    消费者组ID
     * @param messages           存放初始化后message的List
     * @param consumerContextMap 存放消费者上下文的Map，key为messageUUID
     */
    private void initMessage(List<ConsumerRecord<?, ?>> records, String consumerGroupID, List<T> messages, Map<String, ConsumerContext> consumerContextMap) {
        records.forEach(record -> {
            if (!versionSupported(record, consumerGroupID)) {
                return;
            }
            // 生成一个消费者上下文
            ConsumerContext consumerContext = ConsumerContext.createConsumerContext();
            consumerContext.startInit();
            String recordKey = Objects.toString(record.key(), null);
            String recordValue = Objects.toString(record.value(), "");
            try {
                T message = messageParser.parse(recordKey, recordValue, record.headers(), getMessageClass());
                message.setRecordValue(record.value());
                if (!isEventApplicable(message)) {
                    return;
                }

                consumerContext.setMessageVersion(message.getVersion());
                consumerContext.setMessageUUID(message.getMessageUUID());
                consumerContext.setTrackingID(message.getTrackingID());
                consumerContext.setOperationType(message.getOperationType().name());
                consumerContext.setActionType(message.getActionTypeStr());
                consumerContext.setSiteUUID(message.getCurrentSiteUUID());
                consumerContext.setResourceID(message.getResourceID());
                consumerContext.setResourceType(message.getResourceType().toString().toLowerCase());
                consumerContext.setMessageCategory(message.getMessageCategory().name());

                messageValidator.validateMessageContent(message);

                messages.add(message);
            } catch (KafkaMessageParseException pe) {
                handleMessageParseException(record, consumerContext, pe);
            } catch (KafkaMessageValidationException ve) {
                handleMessageValidationException(record, consumerContext, ve);
            }

            consumerContext.setTopic(record.topic());
            consumerContext.setPartition(record.partition());
            consumerContext.setOffset(record.offset());
            consumerContext.setKey(record.key());
            consumerContext.setValue(recordValue);
            consumerContext.setConsumerGroup(consumerGroupID);

            if (StringUtils.isBlank(consumerContext.getResourceType())) {
                consumerContext.setResourceType(KafkaCommonUtil.getResourceTypeFromTopic(record.topic()));
            }
            consumerContext.endInit();

            consumerContextMap.put(consumerContext.getMessageUUID(), consumerContext);
        });
    }

    /**
     * 处理消息
     *
     * @param messages           消息集合
     * @param consumerContextMap 消费者上下文集合
     * @param ack                ack
     */
    private void processMessage(List<T> messages, Map<String, ConsumerContext> consumerContextMap, Acknowledgment ack) {
        if (messages.isEmpty()) {
            if (ack != null) {
                ack.acknowledge();
            }
            return;
        }

        List<T> originalMessages = new ArrayList<>(messages);

        Map<String, ConsumeResult> consumeResultMap;
        try {
            consumeResultMap = process(messages);
        } catch (Exception e) {
            LOGGER.error("Message Process Error!", e);
            consumeResultMap = buildConsumeResultMapForException(originalMessages, e.getMessage());
        } finally {
            if (ack != null) {
                ack.acknowledge();
            }
        }

        // 设置消费者上下文中每个对象的消费结果，consumerContextMap和consumeResultMap的key一致
        for (Map.Entry<String, ConsumeResult> entry : consumeResultMap.entrySet()) {
            consumerContextMap.get(entry.getKey()).setConsumeResult(entry.getValue());
        }
    }

    /**
     * 将消费结果生成日志
     *
     * @param consumerContextMap 消费者上下文集合
     */
    private void logConsumeResult(Map<String, ConsumerContext> consumerContextMap) {
        writeTelemetryLogs(consumerContextMap);
    }

    /**
     * record的version判断
     *
     * @param record          consumer record
     * @param consumerGroupID 消费者组ID
     * @return 是否满足版本支持条件
     */
    private boolean versionSupported(ConsumerRecord<?, ?> record, String consumerGroupID) {
        String currentRecordVersion;
        try {
            currentRecordVersion = getVersionFromConsumerRecord(record);
            MessageVersion minSupportedMessageVersion = getMinSupportedMessageVersion();
            if (minSupportedMessageVersion.isLessThanOrEquals(Double.parseDouble(currentRecordVersion))) {
                return true;
            }

            // 不支持的版本
            ErrorCode errorCode = ErrorCode.builder()
                    .errorType(ErrorType.KAFKA_MESSAGE_CHECK_VERSION_EXCEPTION)
                    .errorDetail(ErrorDetail.MESSAGE_VERSION_NOT_SUPPORTED)
                    .build();

            ErrorSummary errorSummary = ErrorSummary.builder()
                    .prefix("[Notification_Version_Control]")
                    .errorCode(errorCode)
                    .messageUUID(getMessageUUIDFromConsumerRecord(record))
                    .trackingID(getTrackingIDFromConsumerRecord(record))
                    .version(currentRecordVersion)
                    .additionalInfo("consumerGroup", consumerGroupID)
                    .additionalInfo("minSupportVersion", String.valueOf(minSupportedMessageVersion.getValue()))
                    .additionalInfo("currentProcessor", this.getClass().getSimpleName())
                    .build();
            LOGGER.info(errorSummary.getErrorSummaryInfo());
        } catch (Exception e) {
            LOGGER.error("Invalid Version!", e);
        }
        return false;
    }

    /**
     * message的event适用性判断
     *
     * @param message message
     * @return 是否适用event
     */
    protected final boolean isEventApplicable(T message) {
        List<KafkaBusinessEvent> applicableEvents = getApplicableEvents();
        if (CollectionUtils.isEmpty(applicableEvents)) {
            return false;
        }

        for (KafkaBusinessEvent kafkaBusinessEvent : applicableEvents) {
            if (kafkaBusinessEvent != null && kafkaBusinessEvent.matchThisEvent(message)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回当前message适用的event，默认全部适用，可被子类重写
     *
     * @return 使用的event集合
     */
    protected List<KafkaBusinessEvent> getApplicableEvents() {
        return Collections.singletonList(KafkaBusinessEvent.ALL_EVENTS);
    }

    /**
     * KafkaMessageParseException处理
     *
     * @param record          record
     * @param consumerContext 消费者上下文
     * @param ke              消息解析异常
     */
    private void handleMessageParseException(ConsumerRecord<?, ?> record, ConsumerContext consumerContext, KafkaMessageParseException ke) {
        String version = getVersionFromConsumerRecord(record);
        String messageUUID = getMessageUUIDFromConsumerRecord(record);
        String trackingID = getTrackingIDFromConsumerRecord(record);
        long timestamp = getMessageTimestamp(record);
        String siteUUID = getSiteUUIDFromConsumerRecord(record);

        ErrorSummary errorSummary = ErrorSummary.builder()
                .prefix("[Kafka Message Parser]")
                .errorCode(ke.getErrorCode())
                .reason(ke.getMessage())
                .messageUUID(messageUUID)
                .version(version)
                .trackingID(trackingID).build();
        LOGGER.error(errorSummary.getErrorSummaryInfo(), ke);

        consumerContext.setMessageVersion(version);
        consumerContext.setMessageUUID(messageUUID);
        consumerContext.setTrackingID(trackingID);
        consumerContext.setOperationType(getOperationTypeFromConsumerRecord(record));
        consumerContext.setActionType(getActionTypeFromConsumerRecord(record));
        consumerContext.setSiteUUID(siteUUID);
        consumerContext.setConsumeResult(ConsumeResult.createConsumeResult(timestamp).generateFailureResult(errorSummary.getErrorMessage(), ke.getErrorCode()));
    }

    /**
     * KafkaMessageValidationException处理
     *
     * @param record          record
     * @param consumerContext 消费者上下文
     * @param ve              消息验证异常
     */
    private void handleMessageValidationException(ConsumerRecord<?, ?> record, ConsumerContext consumerContext, KafkaMessageValidationException ve) {
        long timestamp = getMessageTimestamp(record);

        ErrorSummary errorSummary = ErrorSummary.builder()
                .prefix("[Kafka Message Validator]")
                .errorCode(ve.getErrorCode())
                .reason(ve.getMessage())
                .messageUUID(consumerContext.getMessageUUID())
                .version(consumerContext.getMessageVersion())
                .trackingID(consumerContext.getTrackingID()).build();
        LOGGER.error(errorSummary.getErrorSummaryInfo(), ve);

        consumerContext.setConsumeResult(ConsumeResult.createConsumeResult(timestamp).generateFailureResult(errorSummary.getErrorMessage(), ve.getErrorCode()));
    }

    /**
     * 获取record中的timestamp
     *
     * @param record record
     * @return timestamp
     */
    private long getMessageTimestamp(ConsumerRecord<?, ?> record) {
        try {
            String timestampStr = getTimestampFromConsumerRecord(record);
            return Long.parseLong(timestampStr);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * 消息处理发生异常时，对消息生成消费结果
     *
     * @param messages         消息集合
     * @param exceptionMessage 异常信息
     * @return 消费结果集合
     */
    private Map<String, ConsumeResult> buildConsumeResultMapForException(List<T> messages, String exceptionMessage) {
        Map<String, ConsumeResult> consumeResultMap = new HashMap<>(messages.size());
        messages.forEach(message -> consumeResultMap.put(message.getMessageUUID(),
                ConsumeResult.createConsumeResult(message).generateFailureResult(exceptionMessage)));
        return consumeResultMap;
    }

    /**
     * 将消费结果生成日志
     *
     * @param consumerContextMap 消费者上下文集合
     */
    private void writeTelemetryLogs(Map<String, ConsumerContext> consumerContextMap) {
        for (Map.Entry<String, ConsumerContext> entry : consumerContextMap.entrySet()) {
            ConsumerContext consumerContext = entry.getValue();

            KafkaConsumerTelemetryLog telemetryLog = KafkaConsumerTelemetryLogFactory.createLog(getTelemetryFeatureName(), consumerContext);

            telemetryLog.setMessageVersion(Objects.toString(consumerContext.getMessageVersion(), ""));
            telemetryLog.setMessageUUID(Objects.toString(consumerContext.getMessageUUID(), ""));
            telemetryLog.setTrackingID(Objects.toString(consumerContext.getTrackingID(), ""));
            telemetryLog.setOperationType(Objects.toString(consumerContext.getOperationType()));
            telemetryLog.setActionType(Objects.toString(consumerContext.getActionType()));
            telemetryLog.setResourceID(Objects.toString(consumerContext.getResourceID()));
            telemetryLog.setResourceType(consumerContext.getResourceType());
            telemetryLog.setSiteUUID(Objects.toString(consumerContext.getSiteUUID()));
            telemetryLog.setNotificationDestination(getTelemetryNotificationDestination());
            telemetryLog.setMessageCategory(Objects.toString(consumerContext.getMessageCategory(), ""));
            telemetryLog.setConsumerGroup(Objects.toString(consumerContext.getConsumerGroup(), ""));
            telemetryLog.setCostTime(consumerContext.getConsumerCostTime());
            telemetryLog.setConsumerCostTime(consumerContext.getConsumerCostTime());
            telemetryLog.setE2EChannelCostTime(consumerContext.getE2EChannelCostTime());

            if (consumeResultIsNull(consumerContext.getConsumeResult(), telemetryLog)) {
                return;
            }

            // 根据消费结果，生成不同的日志
            switch (consumerContext.getConsumeResult().getResult()) {
                case SUCCESS:
                    telemetryLog.logSuccessStatus();
                    break;
                case IGNORE:
                    telemetryLog.logIgnoreStatus(Objects.toString(consumerContext.getConsumeResult().getAdditionalMessage(), ""));
                    break;
                default:
                    telemetryLog.logFailureStatus(Objects.toString(consumerContext.getConsumeResult().getAdditionalMessage(), ""));
            }
        }
    }

    /**
     * 获取feature name
     *
     * @return feature name
     */
    protected final String getTelemetryFeatureName() {
        return getMessageClass().getSimpleName().replace("Message", "Notification");
    }

    /**
     * 获取通知的目标
     *
     * @return notification destination name
     */
    protected final String getTelemetryNotificationDestination() {
        return kafkaConsumerHealthCheck.getComponentName();
    }

    /**
     * 判断消费结果是否为空
     *
     * @param consumeResult 消费结果
     * @param telemetryLog  消费日志
     * @return 消费结果是否为空
     */
    private boolean consumeResultIsNull(ConsumeResult consumeResult, KafkaConsumerTelemetryLog telemetryLog) {
        if (consumeResult == null || consumeResult.getResult() == null) {
            String telemetryLogStr = null;
            try {
                telemetryLogStr = PlatFormJsonUtil.serializeToJson(telemetryLog);
            } catch (Exception e) {
                LOGGER.error("Parse KafkaConsumerTelemetryLog Error! TelemetryLog is: " + telemetryLog.toString());
            }
            LOGGER.error("ConsumeResult or ResultType of consumeResult is null, telemetryLog is: " + telemetryLogStr);
            return true;
        }
        return false;
    }

    private String getVersionFromConsumerRecord(ConsumerRecord<?, ?> record) {
        String version = KafkaCommonUtil.getItemValueFromConsumerRecord(record, "version");
        return StringUtils.isBlank(version) ? "1.0" : (version);
    }

    private String getMessageUUIDFromConsumerRecord(ConsumerRecord<?, ?> record) {
        return KafkaCommonUtil.getItemValueFromConsumerRecord(record, "messageUUID");
    }

    private String getTrackingIDFromConsumerRecord(ConsumerRecord<?, ?> record) {
        return KafkaCommonUtil.getItemValueFromConsumerRecord(record, "trackingID");
    }

    private String getOperationTypeFromConsumerRecord(ConsumerRecord<?, ?> record) {
        return KafkaCommonUtil.getItemValueFromConsumerRecord(record, "operationType");
    }

    private String getActionTypeFromConsumerRecord(ConsumerRecord<?, ?> record) {
        return KafkaCommonUtil.getItemValueFromConsumerRecord(record, "actionType");
    }

    private String getTimestampFromConsumerRecord(ConsumerRecord<?, ?> record) {
        return KafkaCommonUtil.getItemValueFromConsumerRecord(record, "timestamp");
    }

    private String getSiteUUIDFromConsumerRecord(ConsumerRecord<?, ?> record) {
        return KafkaCommonUtil.getItemValueFromConsumerRecord(record, "siteUUID");
    }
}
