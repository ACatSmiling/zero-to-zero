package cn.zero.cloud.platform.exception.type.impl;

import cn.zero.cloud.platform.exception.type.CommonException;

import java.io.Serial;
import java.util.*;

/**
 * @author Xisun Wang
 * @since 2024/3/26 16:04
 */
public class RestResponseException extends RuntimeException implements CommonException {
    @Serial
    private static final long serialVersionUID = 7334456646535372527L;

    private final int status;

    private static final String KEY = "description_";

    private Map<String, String> additionalMessages;

    public RestResponseException(int status, String message) {
        super(message);
        this.status = status;
    }

    public RestResponseException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    @Override
    public void addAdditionalMessage(String description) {
        if (additionalMessages == null) {
            additionalMessages = new LinkedHashMap<>();
        }
        int index = additionalMessages.size() + 1;
        additionalMessages.put(KEY + index, description);
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getAdditionalMessages() {
        return additionalMessages;
    }
}
