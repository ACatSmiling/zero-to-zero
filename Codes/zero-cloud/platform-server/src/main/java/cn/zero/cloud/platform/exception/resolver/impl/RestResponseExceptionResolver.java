package cn.zero.cloud.platform.exception.resolver.impl;

import cn.zero.cloud.platform.exception.type.impl.RestResponseException;
import cn.zero.cloud.platform.exception.resolver.AbstractExceptionResolver;
import cn.zero.cloud.platform.utils.PlatFormDateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Xisun Wang
 * @since 2024/3/26 16:05
 */
@Component("restResponseExceptionResolver")
public class RestResponseExceptionResolver extends AbstractExceptionResolver {
    private static final String CUSTOMIZE_STATUS = "customizeStatus";

    @Override
    protected String getExceptionResolverName() {
        return "restResponseExceptionResolver";
    }

    @Override
    public ResponseEntity<Object> getResponseEntity(Exception e) {
        RestResponseException ex = (RestResponseException) e;
        Map<String, Object> body = generateMessageBody(ex.getMessage());
        Map<String, String> additionalMessages = ex.getAdditionalMessages();
        int customizeStatus = ex.getStatus();
        HttpStatus httpStatus = HttpStatus.resolve(customizeStatus);
        if (httpStatus == null) {
            body.put(CUSTOMIZE_STATUS, customizeStatus);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        if (additionalMessages != null && !additionalMessages.isEmpty()) {
            body.put(ADDITIONAL_MESSAGES, additionalMessages);
        }
        body.put(TIMESTAMP, PlatFormDateUtil.getCurrentTimeDefaultTimeZone());
        return ResponseEntity.status(httpStatus).body(body);
    }
}
