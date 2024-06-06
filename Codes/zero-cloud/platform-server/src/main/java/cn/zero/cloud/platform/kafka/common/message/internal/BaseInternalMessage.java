package cn.zero.cloud.platform.kafka.common.message.internal;


import cn.zero.cloud.platform.kafka.common.message.BaseMessage;
import cn.zero.cloud.platform.kafka.common.constants.MessageCategory;

/**
 * @author Xisun Wang
 * @since 2024/3/8 16:58
 */
public abstract class BaseInternalMessage<A> extends BaseMessage<A> {
    @Override
    public MessageCategory getMessageCategory() {
        return MessageCategory.INTERNAL;
    }
}
