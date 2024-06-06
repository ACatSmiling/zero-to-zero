package cn.zero.cloud.platform.kafka.consumer.healthcheck;

import cn.zero.cloud.platform.kafka.common.pojo.healthcheck.HealthCheckType;
import cn.zero.cloud.platform.kafka.common.pojo.healthcheck.HealthCheckSummary;
import cn.zero.cloud.platform.kafka.consumer.healthcheck.component.KafkaConsumerHealthCheck;
import cn.zero.cloud.platform.kafka.common.constants.ResultType;
import cn.zero.cloud.platform.kafka.common.pojo.result.impl.HealthCheckResult;
import cn.zero.cloud.platform.utils.PlatFormJsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xisun Wang
 * @since 2024/3/8 15:29
 */
@Service
public class KafkaConsumerHealthCheckContainerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerHealthCheckContainerService.class);

    private final List<KafkaConsumerHealthCheck> kafkaConsumerHealthChecks;

    @Autowired
    public KafkaConsumerHealthCheckContainerService(List<KafkaConsumerHealthCheck> kafkaConsumerHealthChecks) {
        this.kafkaConsumerHealthChecks = kafkaConsumerHealthChecks;
    }

    /**
     * 健康检查下游组件
     *
     * @param healthCheckType 监控检查类型
     * @return 健康检查总结
     */
    public HealthCheckSummary checkDownstreamComponents(HealthCheckType healthCheckType) {
        if (healthCheckType == null) {
            healthCheckType = HealthCheckType.LIVENESS_CHECK;
        }
        HealthCheckSummary healthCheckSummary = HealthCheckSummary.createHealthCheckSummary();
        healthCheckSummary.setStatus(healthCheckType.getSuccessStatus());
        List<HealthCheckResult> results = new ArrayList<>();
        if (healthCheckType != HealthCheckType.LIVENESS_CHECK) {
            for (KafkaConsumerHealthCheck component : kafkaConsumerHealthChecks) {
                HealthCheckResult result = component.healthCheck();
                if (ResultType.FAILURE == result.getResult()) {
                    healthCheckSummary.setStatus(healthCheckType.getFailureStatus());
                }
                results.add(result);
            }
        }
        healthCheckSummary.setDetails(results);

        logHealthCheckSummary(healthCheckType.getLogPrefix(), healthCheckSummary);

        return healthCheckSummary;
    }

    private void logHealthCheckSummary(String logPrefix, HealthCheckSummary healthCheckSummary) {
        LOGGER.info("LogPrefix: {}, HealthCheckSummary: {}", logPrefix, PlatFormJsonUtil.serializeToJson(healthCheckSummary));
    }
}
