package cn.zero.cloud.platform.kafka.common.exception.error;

/**
 * @author Xisun Wang
 * @since 2024/3/8 14:59
 */
public enum ErrorType {
    UNKNOWN(99),// Exception.class

    NOTIFICATION_EXCEPTION(0),// NotificationException.class

    KAFKA_MESSAGE_CHECK_VERSION_EXCEPTION(1),// KafkaMessageParseException.class

    KAFKA_MESSAGE_PARSE_EXCEPTION(2),// KafkaMessageParseException.class

    KAFKA_MESSAGE_VALIDATION_EXCEPTION(3),// KafkaMessageValidationException.class

    DATA_VALIDATION_EXCEPTION(10),// DataValidationException.class

    ;

    private final int code;

    ErrorType(int code) {
        assert code >= 0 && code <= 99;
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
