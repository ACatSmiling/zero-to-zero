package cn.zero.cloud.platform.kafka.common.constants;

import static cn.zero.cloud.platform.common.constants.ExceptionConstants.UTILITY_CLASS;

/**
 * @author Xisun Wang
 * @since 2024/3/8 15:29
 */
public class KafkaCommonConstants {
    public static final String TRACKING_ID = "trackingID";

    public static final String HEALTH_CHECK_STATUS_OK = "OKOKOK";

    public static final String HEALTH_CHECK_STATUS_FAIL = "FAIL";

    public static final String NEW_HEALTH_CHECK_STATUS_UP = "UP";

    public static final String NEW_HEALTH_CHECK_STATUS_DOWN = "DOWN";

    public static final String HEALTH_CHECK_LOG_PREFIX = "[Kafka_Consumer_HealthCheck_Summary]";

    public static final String LIVENESS_CHECK_LOG_PREFIX = "[Kafka_Consumer_Liveness_Check_Summary]";

    public static final String READINESS_CHECK_LOG_PREFIX = "[Kafka_Consumer_Readiness_Check_Summary]";

    private KafkaCommonConstants() {
        throw new IllegalStateException(UTILITY_CLASS);
    }
}
