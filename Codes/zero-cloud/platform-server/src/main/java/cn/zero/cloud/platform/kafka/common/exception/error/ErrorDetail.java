package cn.zero.cloud.platform.kafka.common.exception.error;

/**
 * @author Xisun Wang
 * @since 2024/3/8 14:59
 */
public enum ErrorDetail {
    COMMON(0),

    UNKNOWN(999),

    PARSE_KAFKA_MESSAGE_FAILED(11),

    MESSAGE_VERSION_NOT_SUPPORTED(12),

    VALIDATE_KAFKA_MESSAGE_FAILED(13),

    ;

    private final int code;

    ErrorDetail(int code) {
        assert code >= 0 && code <= 999;
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
