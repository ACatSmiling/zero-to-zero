package cn.zero.cloud.platform.kafka.producer.telemetry;

import cn.zero.cloud.platform.kafka.common.message.BaseMessage;
import cn.zero.cloud.platform.kafka.common.telemetry.TelemetryConstants;
import cn.zero.cloud.platform.kafka.common.telemetry.TelemetryStatus;
import cn.zero.cloud.platform.kafka.utils.RecordHeadersUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class ProducerTelemetryLogFactory {
    private static final String DEFAULT_FEATURE_NAME = "Unknown";

    private ProducerTelemetryLogFactory() {
        throw new UnsupportedOperationException();
    }

    public static KafkaProducerTelemetryLog createLog(String topic, BaseMessage message, TelemetryStatus status) {
        String featureName = buildFeatureName(message.getResourceType().name());
        KafkaProducerTelemetryLog telemetryLog = new KafkaProducerTelemetryLog(featureName);
        telemetryLog.setMessageUUID(message.getMessageUUID());
        telemetryLog.setStatus(status.getValue());
        telemetryLog.setTrackingID(message.getTrackingID());

        telemetryLog.setMessageVersion(message.getVersion());
        telemetryLog.setTopicName(topic);
        telemetryLog.setResourceType(Objects.toString(message.getResourceType()));
        telemetryLog.setResourceID(message.getResourceID());
        telemetryLog.setOperationType(Objects.toString(message.getOperationType()));

        telemetryLog.setActionType(Objects.toString(message.getActionTypeStr()));
        telemetryLog.setSiteUUID(message.getCurrentSiteUUID());

        // log produce cost time
        String messageRoutingTime = RecordHeadersUtil.getValueFromHeader(message.getRecordHeaders(), TelemetryConstants.KAFKACOMMON_MSG_PRODUCETIME);
        if (StringUtils.isNotEmpty(messageRoutingTime)) {
            long landingEndTime = System.currentTimeMillis();
            telemetryLog.setCostTime(landingEndTime - Long.parseLong(messageRoutingTime));
        }

        if (StringUtils.isNotEmpty(message.getUrl())) {
            telemetryLog.setUrl(message.getUrl());
        }

        return telemetryLog;
    }

    public static KafkaProducerTelemetryLog createLog(String topic, BaseMessage message, TelemetryStatus status, String errorMessage) {
        KafkaProducerTelemetryLog telemetryLog = createLog(topic, message, status);
        telemetryLog.setErrorMsg(errorMessage);
        return telemetryLog;
    }

    private static String buildFeatureName(String resourceType) {
        if (StringUtils.isBlank(resourceType)) {
            return DEFAULT_FEATURE_NAME;
        }
        return StringUtils.capitalize(resourceType) + "Notification";
    }
}
