package cn.zero.cloud.platform.kafka.common.message.util.validator;

import cn.zero.cloud.platform.kafka.common.event.KafkaBusinessEvent;
import cn.zero.cloud.platform.kafka.common.constants.type.KafkaEventListType;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * @author Xisun Wang
 * @since 2024/3/8 16:19
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotEmptyUnderConditions.ConditionalValidator.class)
@Documented
public @interface NotEmptyUnderConditions {

    String message() default "could not be empty under this condition!";

    double fromVersion() default 1.0;

    KafkaBusinessEvent[] kafkaEventList() default {};

    KafkaEventListType listType() default KafkaEventListType.EXCLUDE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ConditionalValidator extends ConditionChecker implements ConstraintValidator<NotEmptyUnderConditions, String> {
        private double fromVersion;

        private KafkaBusinessEvent[] kafkaEventList;

        private KafkaEventListType listType;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (matchCondition(fromVersion, kafkaEventList, listType)) {
                return !StringUtils.isEmpty(value);
            }
            return true;
        }

        @Override
        public void initialize(NotEmptyUnderConditions constraintAnnotation) {
            this.fromVersion = constraintAnnotation.fromVersion();
            this.kafkaEventList = constraintAnnotation.kafkaEventList();
            this.listType = constraintAnnotation.listType();
        }
    }
}
