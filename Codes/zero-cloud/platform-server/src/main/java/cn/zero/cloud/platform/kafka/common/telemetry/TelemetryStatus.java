package cn.zero.cloud.platform.kafka.common.telemetry;

/**
 * @author Xisun Wang
 * @since 2024/3/11 9:24
 */
public enum TelemetryStatus {
    INITIATE(TelemetryConstants.STATUS_INITIATE),

    SUCCESS(TelemetryConstants.STATUS_SUCCESS),

    IGNORE(TelemetryConstants.STATUS_IGNORE),

    FAILURE(TelemetryConstants.STATUS_FAILURE),

    PENDING_RETRY(TelemetryConstants.STATUS_PENDING_RETRY),

    PENDING_RESTORE(TelemetryConstants.STATUS_PENDING_RESTORE);

    private final String value;

    TelemetryStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
