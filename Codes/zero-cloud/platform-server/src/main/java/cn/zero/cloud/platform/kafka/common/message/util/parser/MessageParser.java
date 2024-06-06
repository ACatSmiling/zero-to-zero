package cn.zero.cloud.platform.kafka.common.message.util.parser;

import cn.zero.cloud.platform.kafka.common.message.BaseMessage;
import cn.zero.cloud.platform.kafka.common.message.util.threadlocal.MessageThreadLocal;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorDetail;
import cn.zero.cloud.platform.kafka.common.exception.KafkaMessageParseException;
import cn.zero.cloud.platform.utils.PlatFormJsonUtil;
import org.apache.kafka.common.header.Headers;
import org.springframework.stereotype.Component;

/**
 * @author Xisun Wang
 * @since 2024/3/8 15:16
 */
@Component
public class MessageParser<T extends BaseMessage> {
    /**
     * 解析record
     *
     * @param recordKey     recordKey
     * @param recordValue   recordValue
     * @param recordHeaders recordHeaders
     * @param messageClazz  消息类型
     * @return message
     * @throws KafkaMessageParseException 解析异常
     */
    public T parse(String recordKey, String recordValue, Headers recordHeaders, Class<T> messageClazz) throws KafkaMessageParseException {
        try {
            T message = PlatFormJsonUtil.deserializeToClassType(recordValue, messageClazz);
            message.setRecordHeaders(recordHeaders);
            message.setRecordKey(recordKey);
            message.compatibleWithDeprecatedFields();
            message.handleRequiredFields();
            buildMessageThreadLocal(message);
            return message;
        } catch (Exception e) {
            String errorMessage = String.format("Failed to parse message! messageClass: %s, details: %s.", messageClazz.getSimpleName(), e.getLocalizedMessage());
            throw new KafkaMessageParseException(errorMessage, ErrorDetail.PARSE_KAFKA_MESSAGE_FAILED, e, false);
        }
    }

    /**
     * 构建current message的ThreadLocal信息
     *
     * @param message current message
     */
    private void buildMessageThreadLocal(T message) {
        MessageThreadLocal.clear();
        MessageThreadLocal.setMessageClassName(message.getClass().getCanonicalName());
        MessageThreadLocal.setVersion(message.getVersion());
        MessageThreadLocal.setOperationType(message.getOperationType());
        MessageThreadLocal.setActionType(message.getActionTypeStr());
    }
}
