package cn.zero.cloud.platform.kafka.common.exception;

import cn.zero.cloud.platform.kafka.common.exception.error.ErrorCode;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorDetail;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorType;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Xisun Wang
 * @since 2024/3/8 15:06
 */
public class KafkaProducerException extends Exception implements Serializable {
    @Serial
    private static final long serialVersionUID = 1260136001543153075L;

    protected ErrorCode errorCode;

    public KafkaProducerException(String message) {
        super(message);
        buildErrorCode(ErrorDetail.UNKNOWN, null);
    }

    public KafkaProducerException(Throwable cause) {
        super(cause);
        buildErrorCode(ErrorDetail.UNKNOWN, null);
    }

    public KafkaProducerException(String message, Throwable cause) {
        super(message, cause);
        buildErrorCode(ErrorDetail.UNKNOWN, null);
    }

    public KafkaProducerException(String message, ErrorDetail errorDetail, Throwable cause) {
        super(message, cause);
        this.buildErrorCode(errorDetail, null);
    }

    public KafkaProducerException(String message, ErrorDetail errorDetail, Boolean needRetry) {
        super(message);
        buildErrorCode(errorDetail, needRetry);
    }

    public KafkaProducerException(String message, ErrorDetail errorDetail, Throwable cause, Boolean needRetry) {
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
