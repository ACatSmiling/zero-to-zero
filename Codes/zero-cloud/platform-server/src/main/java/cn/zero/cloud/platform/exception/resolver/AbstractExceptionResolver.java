package cn.zero.cloud.platform.exception.resolver;

import cn.zero.cloud.platform.utils.PlatFormJsonUtil;
import cn.zero.cloud.platform.exception.type.impl.PlatFormJsonException;
import cn.zero.cloud.platform.utils.PlatFormDateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.*;

/**
 * 异常处理器
 *
 * @author Xisun Wang
 * @since 2024/3/26 16:03
 */
public abstract class AbstractExceptionResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExceptionResolver.class);

    protected static final String MESSAGE = "message";

    protected static final String KEY = "description_";

    private static final String RESPONSE_STATUS = "responseStatus";

    protected static final String ADDITIONAL_MESSAGES = "additionalMessages";

    protected static final String TIMESTAMP = "timestamp";

    /**
     * 获得异常处理器名称
     *
     * @return 异常处理器名称
     */
    protected abstract String getExceptionResolverName();

    /**
     * 获取异常对应的响应体
     *
     * @param e 待处理的异常
     * @return 响应体
     */
    protected abstract ResponseEntity<Object> getResponseEntity(Exception e);

    /**
     * 获取异常对应的响应内容，默认实现
     *
     * @param e 待处理的异常
     * @return 响应内容
     */
    protected String getResponseJsonResult(Exception e) {
        PlatFormJsonException ex = (PlatFormJsonException) e;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(RESPONSE_STATUS, ex.getStatus());
        body.put(ADDITIONAL_MESSAGES, ex.getAdditionalMessages());
        body.put(TIMESTAMP, PlatFormDateUtil.getCurrentTimeDefaultTimeZone());
        return PlatFormJsonUtil.serializeToJson(body);
    }

    /**
     * 记录并处理异常，返回响应体
     *
     * @param e 待处理的异常
     * @return 响应体
     */
    public ResponseEntity<Object> handleException(Exception e) {
        LOGGER.error(getExceptionResolverName(), e);
        return getResponseEntity(e);
    }

    /**
     * 记录并处理异常，返回字符串
     *
     * @param e 待处理的异常
     * @return 响应内容
     */
    public String handleJsonException(Exception e) {
        LOGGER.error("platFormJsonExceptionResolver", e);
        return getResponseJsonResult(e);
    }

    /**
     * 生成响应体的body
     *
     * @param message 异常信息描述
     * @return 响应体的body
     */
    protected Map<String, Object> generateMessageBody(String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(MESSAGE, message);
        return body;
    }

    protected Map<String, Object> setMessage(String errorMessage, Map<String, Object> map) {
        if (map != null) {
            map.put(MESSAGE, errorMessage);
        }
        return map;
    }

    /**
     * 添加异常的附加信息
     *
     * @param body        消息体
     * @param description 信息描述
     */
    protected void addAdditionalMessage(Map<String, Object> body, String description) {
        if (body == null || StringUtils.isBlank(description)) {
            return;
        }

        Map<String, String> additionMessages;
        if (body.containsKey(ADDITIONAL_MESSAGES)) {
            additionMessages = (Map<String, String>) body.get(ADDITIONAL_MESSAGES);
        } else {
            additionMessages = new LinkedHashMap<>();
        }
        int index = additionMessages.size() + 1;
        additionMessages.put(KEY + index, description);
        body.put(ADDITIONAL_MESSAGES, additionMessages);
    }
}
