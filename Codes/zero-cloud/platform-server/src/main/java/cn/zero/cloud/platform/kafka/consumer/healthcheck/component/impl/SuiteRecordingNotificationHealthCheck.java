package cn.zero.cloud.platform.kafka.consumer.healthcheck.component.impl;

import cn.zero.cloud.platform.kafka.common.pojo.result.impl.HealthCheckResult;
import cn.zero.cloud.platform.kafka.consumer.healthcheck.component.KafkaConsumerHealthCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Xisun Wang
 * @since 2024/3/11 9:36
 */
@Component
public class SuiteRecordingNotificationHealthCheck extends KafkaConsumerHealthCheck {
    public static final String AI_INTEGRATION_SUITE_RECORDING = "ai-integration-suite-recording";

    /**
     * 是否启用消息通知
     */
    @Value("${zero.cloud.kafka.notification.message.suite-recording.enable:false}")
    private boolean messageNotificationEnable;

    @Override
    public String getComponentName() {
        return AI_INTEGRATION_SUITE_RECORDING;
    }

    @Override
    public boolean isNotificationDisable() {
        return !messageNotificationEnable;
    }

    @Override
    public HealthCheckResult doComponentHealthCheck() {
        return HealthCheckResult.createHealthCheckResult().generateSuccessResult();
    }
}
