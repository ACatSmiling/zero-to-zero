package cn.zero.cloud.platform.kafka.utils;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import java.nio.charset.StandardCharsets;

import static cn.zero.cloud.platform.common.constants.ExceptionConstants.UTILITY_CLASS;

public class RecordHeadersUtil {
    private RecordHeadersUtil() {
        throw new IllegalStateException(UTILITY_CLASS);
    }

    public static String getValueFromHeader(Headers headers, String key) {
        if (null != headers) {
            Header header = headers.lastHeader(key);
            if (header != null) {
                return new String(header.value(), StandardCharsets.UTF_8);
            }
            return "";
        }
        return "";
    }
}
