package cn.zero.cloud.platform.kafka.producer.notification;

import cn.zero.cloud.platform.kafka.utils.KafkaCommonUtil;
import cn.zero.cloud.platform.utils.PlatFormJsonUtil;
import cn.zero.cloud.platform.kafka.common.message.BaseMessage;
import cn.zero.cloud.platform.kafka.common.exception.KafkaProducerException;
import cn.zero.cloud.platform.kafka.common.telemetry.TelemetryConstants;
import cn.zero.cloud.platform.kafka.common.telemetry.TelemetryStatus;
import cn.zero.cloud.platform.kafka.producer.KafkaClientState;
import cn.zero.cloud.platform.kafka.producer.cache.KafkaSenderCacheName;
import cn.zero.cloud.platform.kafka.producer.ratelimit.KafkaSenderRatelimiter;
import cn.zero.cloud.platform.kafka.producer.resending.client.Resender;
import cn.zero.cloud.platform.kafka.producer.telemetry.ProducerTelemetryLogFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.protocol.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * kafka通知消息发送者
 *
 * @author Xisun Wang
 * @since 2024/3/6 14:25
 */
public class KafkaNotificationSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaNotificationSender.class);

    public static final String KAFKA_NOTIFICATION_SENDER = "kafkaNotificationSender";

    private static final int MAX_LIMITATION_MB = 10 * 1048576;

    private static final int MIN_LIMITATION_MB = 1 * 1048576;

    private KafkaClientState kafkaClientState;

    @Autowired(required = false)
    @Qualifier(value = "kafkaNotificationTemplate")
    private KafkaTemplate<Object, BaseMessage<Object>> kafkaNotificationTemplate;

    @Autowired(required = false)
    @Qualifier(value = "kafkaNotificationCompressionTemplate")
    private KafkaTemplate<Object, BaseMessage<Object>> kafkaNotificationCompressionTemplate;

    @Autowired
    KafkaSenderRatelimiter kafkaSenderRatelimiter;

    private boolean enableRetry;

    private Resender resender;

    public void setResender(Resender resender) {
        this.resender = resender;
    }

    public void setEnableRetry(boolean enableRetry) {
        this.enableRetry = enableRetry;
    }

    public void initiateKafkaClientState(Map<String, Object> state) {
        kafkaClientState = KafkaClientState.getInstance();
        kafkaClientState.registerState(KAFKA_NOTIFICATION_SENDER, state);
    }

    public void sendMessage(BaseMessage message, String topic) {
        sendMessage(message, topic, null);
    }

    public void sendMessage(BaseMessage message, String topic, Iterable<Header> headers) {
        if (!kafkaClientState.isEnabled(KAFKA_NOTIFICATION_SENDER)) {
            return;
        }
        String cacheName = KafkaSenderCacheName.findCacheNameBaseOnTypes(message.getResourceType().name(), message.getOperationType().name());
        if (StringUtils.isEmpty(cacheName)) {
            send(message, topic, headers);
        } else {
            String count = kafkaClientState.getRatelimit(KAFKA_NOTIFICATION_SENDER, cacheName);
            boolean needSend = kafkaSenderRatelimiter.isBelowThreshold(cacheName, Integer.parseInt(count), message.getResourceID());
            if (needSend) {
                send(message, topic, headers);
            } else {
                LOGGER.error("drop the message as ratelimit for cacheName {}, count {}, resourceType {},  resourceId {}, operationType {}", cacheName, count
                        , message.getResourceType(), message.getResourceID(), message.getOperationType());
            }
        }
    }

    private void send(BaseMessage message, String topic, Iterable<Header> headers) {
        String servers = kafkaClientState.getServers(KAFKA_NOTIFICATION_SENDER);
        ProducerRecord<Object, BaseMessage<Object>> record;
        try {
            // topic empty check
            if (StringUtils.isEmpty(topic)) {
                logError(topic, message, Errors.INVALID_TOPIC_EXCEPTION.message());
                return;
            }

            // servers empty check
            if (StringUtils.isEmpty(servers)) {
                logError(topic, message, "No resolvable bootstrap urls given in " + ProducerConfig.BOOTSTRAP_SERVERS_CONFIG);
                return;
            }
            record = new ProducerRecord<>(topic, null, message.getProduceRecordKey(), message, wrapRecordHeaders(headers));
            int byteSize = KafkaCommonUtil.getKafkaHeaderString(record.headers()).getBytes().length + PlatFormJsonUtil.serializeToJson(message).getBytes().length;

            if (byteSize >= MIN_LIMITATION_MB && byteSize < MAX_LIMITATION_MB) { // 1MB - 10MB all compress or else > 10MB will drop
                LOGGER.info("big size message can be suitable for compressing byteSize {},trackingId {}", byteSize, message.getTrackingID());
                kafkaNotificationCompressionTemplate.send(record);
                return;
            }

            if (byteSize > MAX_LIMITATION_MB) {
                LOGGER.error("big size message can't be compressed and drop as it will impact downstream byteSize {} ,trackingId {}", byteSize, message.getTrackingID());
                return;
            }
            kafkaNotificationTemplate.send(record);

        } catch (IllegalMonitorStateException e) {
            String msg = "IllegalMonitorStateException case send message error, trackingID {} " + message.getTrackingID();
            LOGGER.error(msg + enableRetry, e.getMessage());
            record = new ProducerRecord<>(topic, null, message.getProduceRecordKey(), message, wrapRecordHeaders(headers));
            if (enableRetry && resender != null) {
                LOGGER.error("enableRetry {}, resender {}, trackingID{}", enableRetry, resender, message.getTrackingID());
                ResponseEntity<String> response = null;
                try {
                    response = resender.resendMessage(record);
                } catch (KafkaProducerException ex) {
                    LOGGER.error(msg, ex);
                    ProducerTelemetryLogFactory.createLog(record.topic(), message, TelemetryStatus.FAILURE, ex.getMessage());
                }
                if (response != null) {
                    LOGGER.error("response status {}, trackingID {}", response.getStatusCode(), message.getTrackingID());
                    ProducerTelemetryLogFactory.createLog(record.topic(), message, response.getStatusCode() == HttpStatus.CREATED ?
                            TelemetryStatus.PENDING_RETRY : TelemetryStatus.FAILURE, e.getMessage());
                } else {
                    ProducerTelemetryLogFactory.createLog(record.topic(), message, TelemetryStatus.FAILURE, e.getMessage());
                }
                return;
            }
            LOGGER.error("end - retry not enable or resender not init trackingID {} ", message.getTrackingID());
            ProducerTelemetryLogFactory.createLog(record.topic(), message, TelemetryStatus.FAILURE, e.getMessage());
            return;

        } catch (Exception e) {
            String msg = "other case send message error not bring reSend trackingID: " + message.getTrackingID();
            LOGGER.error(msg, e.getMessage());
        }
    }

    private void logError(String topic, BaseMessage message, String errorMessage) {
        ProducerTelemetryLogFactory.createLog(topic, message, TelemetryStatus.FAILURE, errorMessage).logInfo();
    }

    private Headers wrapRecordHeaders(Iterable<Header> headers) {
        Headers headerList = new RecordHeaders(headers);
        headerList.add(new RecordHeader(TelemetryConstants.KAFKACOMMON_MSG_PRODUCETIME, Long.toString(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8)));
        return headerList;
    }
}
