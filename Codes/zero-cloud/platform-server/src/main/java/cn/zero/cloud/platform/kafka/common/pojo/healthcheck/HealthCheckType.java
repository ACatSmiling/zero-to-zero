package cn.zero.cloud.platform.kafka.common.pojo.healthcheck;

import static cn.zero.cloud.platform.kafka.common.constants.KafkaCommonConstants.*;

/**
 * @author Xisun Wang
 * @since 2024/3/8 15:29
 */
public enum HealthCheckType {
    /**
     * 传统健康检查
     */
    LEGACY_HEALTH_CHECK(HEALTH_CHECK_STATUS_OK, HEALTH_CHECK_STATUS_FAIL, HEALTH_CHECK_LOG_PREFIX),

    /**
     * 活跃度检查
     */
    LIVENESS_CHECK(NEW_HEALTH_CHECK_STATUS_UP, NEW_HEALTH_CHECK_STATUS_DOWN, LIVENESS_CHECK_LOG_PREFIX),

    /**
     * 准备情况检查
     */
    READINESS_CHECK(NEW_HEALTH_CHECK_STATUS_UP, NEW_HEALTH_CHECK_STATUS_DOWN, READINESS_CHECK_LOG_PREFIX);

    private final String successStatus;

    private final String failureStatus;

    private final String logPrefix;

    HealthCheckType(String successStatus, String failureStatus, String logPrefix) {
        this.successStatus = successStatus;
        this.failureStatus = failureStatus;
        this.logPrefix = logPrefix;
    }

    public String getSuccessStatus() {
        return successStatus;
    }

    public String getFailureStatus() {
        return failureStatus;
    }

    public String getLogPrefix() {
        return logPrefix;
    }
}
