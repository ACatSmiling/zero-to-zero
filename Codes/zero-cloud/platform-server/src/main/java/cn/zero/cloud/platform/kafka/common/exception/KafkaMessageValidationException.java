package cn.zero.cloud.platform.kafka.common.exception;

import cn.zero.cloud.platform.kafka.common.exception.error.ErrorCode;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorType;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorDetail;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Xisun Wang
 * @since 2024/3/8 14:48
 */
public class KafkaMessageValidationException extends KafkaConsumerException implements Serializable {
    @Serial
    private static final long serialVersionUID = 5809646630705142757L;

    public KafkaMessageValidationException(String message) {
        super(message);
    }

    public KafkaMessageValidationException(Throwable cause) {
        super(cause);
    }

    public KafkaMessageValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public KafkaMessageValidationException(String message, ErrorDetail errorDetail, boolean needRetry) {
        super(message, errorDetail, needRetry);
    }

    public KafkaMessageValidationException(String message, ErrorDetail errorDetail, Throwable cause, boolean needRetry) {
        super(message, errorDetail, cause, needRetry);
    }

    @Override
    protected void buildErrorCode(ErrorDetail errorDetail, Boolean needRetry) {
        if (needRetry == null) {
            needRetry = false;
        }
        this.errorCode = ErrorCode.builder()
                .errorType(ErrorType.KAFKA_MESSAGE_VALIDATION_EXCEPTION)
                .errorDetail(errorDetail)
                .needRetry(needRetry)
                .build();
    }

}
