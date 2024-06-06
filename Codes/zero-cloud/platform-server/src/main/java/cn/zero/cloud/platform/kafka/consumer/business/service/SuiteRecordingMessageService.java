package cn.zero.cloud.platform.kafka.consumer.business.service;

import cn.zero.cloud.platform.kafka.common.message.internal.recording.SuiteRecordingMessage;
import cn.zero.cloud.platform.kafka.common.pojo.result.impl.ConsumeResult;
import cn.zero.cloud.platform.kafka.common.telemetry.TelemetryConstants;
import cn.zero.cloud.platform.telemetry.Telemetry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static cn.zero.cloud.platform.common.constants.TelemetryConstants.FeatureType.KAFKA_FEATURE;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.MetricType.KAFKA_METRIC;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.ModuleType.KAFKA_API;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.ObjectType.KAFKA_OBJECT;

/**
 * message业务消费
 *
 * @author Xisun Wang
 * @since 2024/3/8 17:30
 */
@Slf4j
@Service
public class SuiteRecordingMessageService {
    @Telemetry(moduleType = KAFKA_API, metricType = KAFKA_METRIC, featureType = KAFKA_FEATURE, objectType = KAFKA_OBJECT, parameters = {"#message.siteUUID", "#message.meetingInstanceID", "#message.recordingUUID"})
    public ConsumeResult consume(SuiteRecordingMessage message) {
        ConsumeResult result = ConsumeResult.createConsumeResult(message);
        try {
            // 业务处理成功
            log.info("Consume suite recording success! TrackingID: {}", message.getTrackingID());
            result.generateSuccessResult();
        } catch (Exception e) {
            // 业务处理失败
            log.error("Consume suite recording failed! TrackingID: {}", message.getTrackingID(), e);
            result.generateFailureResult("Consume suite recording failed " + e.getMessage());
        }
        return result;
    }
}
