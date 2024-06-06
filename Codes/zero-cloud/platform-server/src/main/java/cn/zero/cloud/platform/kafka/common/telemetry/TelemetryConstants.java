package cn.zero.cloud.platform.kafka.common.telemetry;

public class TelemetryConstants {

    // Common Constants
    public static final String METRIC_NAME = "Kafka_Message_Channel";
    public static final String VERB_PRODUCE = "PRODUCE";
    public static final String VERB_CONSUME = "CONSUME";
    public static final String VERB_MONITOR = "MONITOR";
    public static final String VERB_ROUTE = "ROUTE";

    // Status
    public static final String STATUS_INITIATE = "initiate";
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAILURE = "failure";
    public static final String STATUS_IGNORE = "ignore";
    public static final String STATUS_PENDING_RETRY = "pendingRetry";
    public static final String STATUS_PENDING_RESTORE = "pendingRestore";

    // Record Header Key
    public static final String KAFKACOMMON_MSG_PRODUCETIME = "WbxKafkaCommon-MsgProduceTimestamp";

    protected TelemetryConstants() {
        throw new UnsupportedOperationException();
    }
}
