package cn.zero.cloud.platform.kafka.producer.notification;

import cn.zero.cloud.platform.kafka.common.message.BaseMessage;
import cn.zero.cloud.platform.kafka.common.telemetry.TelemetryStatus;
import cn.zero.cloud.platform.kafka.producer.resending.client.Resender;
import cn.zero.cloud.platform.kafka.producer.telemetry.ProducerTelemetryLogFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.ProducerListener;


public class KafkaNotificationProducerRetryListener<K, V> implements ProducerListener<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(KafkaNotificationProducerRetryListener.class);
    private boolean enableRetry;
    private Resender resender;

    public void setResender(Resender resender) {
        this.resender = resender;
    }

    public void setEnableRetry(boolean enableRetry) {
        this.enableRetry = enableRetry;
    }

    @Override
    public void onError(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        BaseMessage baseMessage  = (BaseMessage) producerRecord.value();
        baseMessage.setRecordHeaders(producerRecord.headers());
        try {
            if (enableRetry && resender != null) {
                ResponseEntity<String> response = resender.resendMessage(producerRecord);
                ProducerTelemetryLogFactory.createLog(producerRecord.topic(), baseMessage, response.getStatusCode() == HttpStatus.CREATED ?
                        TelemetryStatus.PENDING_RETRY : TelemetryStatus.FAILURE, getExceptionKey(exception, "; response code " + response.getStatusCode().value() + ", " + response.getBody())).logInfo();
            } else {
                ProducerTelemetryLogFactory.createLog(producerRecord.topic(), baseMessage, TelemetryStatus.FAILURE, getExceptionKey(exception,null)).logInfo();
            }
        } catch (Exception e) {
            String msg = "Error occurred when handle KafkaProducerListener error, " + baseMessage.getMessageUUID();
            logError(producerRecord.topic(), baseMessage, msg);
            logger.error(msg, e);
        }
    }

    @Override
    public void onSuccess(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata) {
        try {
            BaseMessage baseMessage = (BaseMessage) producerRecord.value();
            baseMessage.setRecordHeaders(producerRecord.headers());
            ProducerTelemetryLogFactory.createLog(producerRecord.topic(), baseMessage, TelemetryStatus.SUCCESS).logInfo();
        } catch (Exception e) {
            logger.error("Error occurred when handle KafkaProducerListener success callback, messageUUID:{}",
                    producerRecord.key().toString(), e);
        }
    }


    private void logError(String topic, BaseMessage message, String errorMessage) {
        ProducerTelemetryLogFactory.createLog(topic, message, TelemetryStatus.FAILURE, errorMessage).logInfo();
    }

    private String getExceptionKey(Exception e,String extraMsg) {
        String exceptionKey;
        String errorMsg = e.getMessage();
        if (errorMsg != null && !"".equals(errorMsg.trim())) {
            if (errorMsg.length() <= 80) {
                exceptionKey = errorMsg;
            } else {
                exceptionKey = errorMsg.substring(0, 80);
            }
        } else {
            exceptionKey = e.getClass().getName();
        }

        if (StringUtils.isNotBlank(extraMsg)) {
            if (extraMsg.length() <= 200) {
                exceptionKey += extraMsg;
            } else {
                exceptionKey += extraMsg.substring(0, 200);
            }
        }
        return exceptionKey;
    }
}
