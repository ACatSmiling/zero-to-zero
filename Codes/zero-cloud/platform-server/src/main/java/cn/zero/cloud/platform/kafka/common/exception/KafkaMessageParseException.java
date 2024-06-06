package cn.zero.cloud.platform.kafka.common.exception;

import cn.zero.cloud.platform.kafka.common.exception.error.ErrorCode;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorDetail;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorType;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Xisun Wang
 * @since 2024/3/8 15:05
 */
public class KafkaMessageParseException extends KafkaConsumerException implements Serializable {
    @Serial
    private static final long serialVersionUID = -8930752629090337092L;

    public KafkaMessageParseException(String message) {
        super(message);
    }

    public KafkaMessageParseException(Throwable cause) {
        super(cause);
    }

    public KafkaMessageParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public KafkaMessageParseException(String message, ErrorDetail errorDetail, Throwable cause, boolean needRetry) {
        super(message, errorDetail, cause, needRetry);
    }

    @Override
    protected void buildErrorCode(ErrorDetail errorDetail, Boolean needRetry) {
        if (needRetry == null) {
            needRetry = false;
        }

        this.errorCode = ErrorCode.builder()
                .errorType(ErrorType.KAFKA_MESSAGE_PARSE_EXCEPTION)
                .errorDetail(errorDetail)
                .needRetry(needRetry)
                .build();
    }
}
