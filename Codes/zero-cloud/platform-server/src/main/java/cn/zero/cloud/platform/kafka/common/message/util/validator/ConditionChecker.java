package cn.zero.cloud.platform.kafka.common.message.util.validator;

import cn.zero.cloud.platform.kafka.common.event.KafkaBusinessEvent;
import cn.zero.cloud.platform.kafka.common.constants.type.KafkaEventListType;
import cn.zero.cloud.platform.kafka.common.constants.OperationType;
import cn.zero.cloud.platform.kafka.utils.KafkaCommonUtil;
import cn.zero.cloud.platform.kafka.common.message.util.threadlocal.MessageThreadLocal;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Xisun Wang
 * @since 2024/3/8 16:29
 */
public abstract class ConditionChecker {
    protected boolean matchCondition(double fromVersion, KafkaBusinessEvent[] kafkaEventList, KafkaEventListType listType) {
        double currentMessageVersionVal = KafkaCommonUtil.parseDoubleWithDefaultValue(
                MessageThreadLocal.getVersion(), 0);

        if (currentMessageVersionVal < fromVersion) {
            return false;
        }

        if (listType == KafkaEventListType.INCLUDE) {
            return isMessageEventInProvidedList(kafkaEventList,
                    MessageThreadLocal.getMessageClassName(), MessageThreadLocal.getOperationType(), MessageThreadLocal.getActionType());
        } else {
            return !isMessageEventInProvidedList(kafkaEventList,
                    MessageThreadLocal.getMessageClassName(), MessageThreadLocal.getOperationType(), MessageThreadLocal.getActionType());
        }
    }

    protected boolean isMessageEventInProvidedList(
            KafkaBusinessEvent[] providedEventList, String messageClassName, OperationType messageOperationType, String messageActionType) {
        if (ArrayUtils.isEmpty(providedEventList)) {
            return false;
        }
        for (KafkaBusinessEvent kafkaBusinessEvent : providedEventList) {
            if (kafkaBusinessEvent != null && kafkaBusinessEvent.matchThisEvent(messageClassName, messageOperationType, messageActionType)) {
                return true;
            }
        }
        return false;
    }
}
