package cn.zero.cloud.platform.kafka.producer.notification;


import cn.zero.cloud.platform.kafka.common.message.BaseMessage;
import cn.zero.cloud.platform.kafka.common.telemetry.TelemetryStatus;
import cn.zero.cloud.platform.kafka.producer.telemetry.ProducerTelemetryLogFactory;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.lang.Nullable;


public class KafkaNotificationProducerListener<K, V> implements ProducerListener<K, V> {
	private static final Logger logger = LoggerFactory.getLogger(KafkaNotificationProducerListener.class);

    @Override
	public void onError(ProducerRecord<K, V> producerRecord, @Nullable RecordMetadata recordMetadata, Exception exception) {
        try {
            BaseMessage baseMessage = (BaseMessage) producerRecord.value();
            ProducerTelemetryLogFactory.createLog(producerRecord.topic(), baseMessage, TelemetryStatus.FAILURE, getExceptionKey(exception)).logInfo();
        } catch (Exception e) {
            logger.error("Error occurred when handle KafkaProducerListener error callback, messageUUID:{}",
                    producerRecord.key().toString(), e);
        }
    }

	@Override
	public void onSuccess(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata) {
		try {
			BaseMessage baseMessage = (BaseMessage) producerRecord.value();
			ProducerTelemetryLogFactory.createLog(producerRecord.topic(), baseMessage, TelemetryStatus.SUCCESS).logInfo();
		} catch (Exception e) {
			logger.error("Error occurred when handle KafkaProducerListener success callback, messageUUID:{}",
					producerRecord.key().toString(), e);
		}
	}

	private String getExceptionKey(Exception e) {
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
		return exceptionKey;
	}
}
