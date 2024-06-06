package cn.zero.cloud.platform.kafka.common.exception;

import cn.zero.cloud.platform.kafka.common.exception.error.ErrorCode;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorDetail;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorType;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Xisun Wang
 * @since 2024/3/8 14:49
 */
public class KafkaConsumerException extends Exception implements Serializable {
    @Serial
    private static final long serialVersionUID = 5356280087343521399L;

    protected ErrorCode errorCode;

    public KafkaConsumerException(String message) {
        super(message);
        buildErrorCode(ErrorDetail.UNKNOWN, null);
    }

    public KafkaConsumerException(Throwable cause) {
        super(cause);
        buildErrorCode(ErrorDetail.UNKNOWN, null);
    }

    public KafkaConsumerException(String message, Throwable cause) {
        super(message, cause);
        buildErrorCode(ErrorDetail.UNKNOWN, null);
    }

    public KafkaConsumerException(String message, ErrorDetail errorDetail, Boolean needRetry) {
        super(message);
        buildErrorCode(errorDetail, needRetry);
    }

    public KafkaConsumerException(String message, ErrorDetail errorDetail, Throwable cause) {
        super(message, cause);
        this.buildErrorCode(errorDetail, null);
    }

    public KafkaConsumerException(String message, ErrorDetail errorDetail, Throwable cause, Boolean needRetry) {
        super(message, cause);
        this.buildErrorCode(errorDetail, needRetry);
    }

    protected void buildErrorCode(ErrorDetail errorDetail, Boolean needRetry) {
        this.errorCode = ErrorCode.builder()
                .errorType(ErrorType.NOTIFICATION_EXCEPTION)
                .errorDetail(errorDetail)
                .needRetry(needRetry)
                .build();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
