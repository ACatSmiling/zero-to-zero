package cn.zero.cloud.platform.kafka.common.message.util.threadlocal;

import cn.zero.cloud.platform.kafka.common.constants.OperationType;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Xisun Wang
 * @since 2024/3/8 16:36
 */
public class MessageThreadLocal {
    private MessageThreadLocal() {
        throw new UnsupportedOperationException();
    }

    private static final ThreadLocal<String> CURRENT_MESSAGE_CLASS_NAME = new ThreadLocal<>();

    private static final ThreadLocal<String> CURRENT_VERSION = new ThreadLocal<>();

    private static final ThreadLocal<OperationType> CURRENT_OPERATION_TYPE = new ThreadLocal<>();

    private static final ThreadLocal<String> CURRENT_ACTION_TYPE = new ThreadLocal<>();

    public static void setMessageClassName(String messageClassName) {
        CURRENT_MESSAGE_CLASS_NAME.set(messageClassName);
    }

    public static String getMessageClassName() {
        return CURRENT_MESSAGE_CLASS_NAME.get();
    }

    public static void setVersion(String version) {
        CURRENT_VERSION.set(version);
    }

    public static String getVersion() {
        String version = CURRENT_VERSION.get();
        return StringUtils.isBlank(version) ? "0" : version;
    }

    public static void setOperationType(OperationType type) {
        CURRENT_OPERATION_TYPE.set(type);
    }

    public static OperationType getOperationType() {
        return CURRENT_OPERATION_TYPE.get();
    }

    public static void setActionType(String actionTypeStr) {
        CURRENT_ACTION_TYPE.set(actionTypeStr);
    }

    public static String getActionType() {
        return CURRENT_ACTION_TYPE.get();
    }

    public static void clear() {
        CURRENT_MESSAGE_CLASS_NAME.remove();
        CURRENT_VERSION.remove();
        CURRENT_OPERATION_TYPE.remove();
        CURRENT_ACTION_TYPE.remove();
    }
}
