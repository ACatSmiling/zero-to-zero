package cn.zero.cloud.platform.kafka.consumer.healthcheck.component;

import cn.zero.cloud.platform.kafka.common.pojo.result.impl.HealthCheckResult;
import org.springframework.stereotype.Component;

/**
 * @author Xisun Wang
 * @since 2024/3/11 9:37
 */
@Component
public abstract class KafkaConsumerHealthCheck {
    /**
     * @return 子类名称
     */
    public abstract String getComponentName();

    public abstract boolean isNotificationDisable();

    /**
     * @return 子类 health check 结果
     */
    public abstract HealthCheckResult doComponentHealthCheck();

    public final HealthCheckResult healthCheck() {
        HealthCheckResult healthCheckResult = HealthCheckResult.createHealthCheckResult();

        // 未启用消息通知
        if (isNotificationDisable()) {
            return healthCheckResult.generateIgnoreResult(getComponentName() + " feature toggle is not enabled!");
        }

        try {
            return doComponentHealthCheck().appendComponentName(getComponentName());
        } catch (Exception e) {
            return healthCheckResult.generateFailureResult(getComponentName() + e.getMessage());
        }
    }
}
