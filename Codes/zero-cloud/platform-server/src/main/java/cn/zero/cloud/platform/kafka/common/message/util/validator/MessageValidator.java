package cn.zero.cloud.platform.kafka.common.message.util.validator;

import cn.zero.cloud.platform.kafka.common.message.BaseMessage;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorDetail;
import cn.zero.cloud.platform.kafka.common.exception.KafkaMessageValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Xisun Wang
 * @since 2024/3/8 15:24
 */
@Component
public class MessageValidator<T extends BaseMessage> {
    private final Validator validator;

    @Autowired
    public MessageValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * 验证消息内容
     *
     * @param message 消息
     * @throws KafkaMessageValidationException 验证异常
     */
    public void validateMessageContent(T message) throws KafkaMessageValidationException {
        // 如果message对象中的属性不满足约束条件，将返回一个包含ConstraintViolation对象的集合
        Set<ConstraintViolation<T>> violations = validator.validate(message);

        // 遍历所有的ConstraintViolation对象，并将每个违反约束的错误信息追加到errorMessages中
        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(" "));

            String errorMessage = String.format("Invalid kafka message content! messageClass: %s, details: %s.",
                    message.getClass().getSimpleName(), errorMessages);

            throw new KafkaMessageValidationException(errorMessage, ErrorDetail.VALIDATE_KAFKA_MESSAGE_FAILED, false);
        }

        // 验证message的actionType
        message.validateActionType();
    }
}
