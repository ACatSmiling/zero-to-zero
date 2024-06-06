package cn.zero.cloud.platform.kafka.utils;

import cn.zero.cloud.platform.utils.PlatFormJsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;

import java.util.HashMap;
import java.util.Map;

import static cn.zero.cloud.platform.common.constants.ExceptionConstants.UTILITY_CLASS;

/**
 * @author Xisun Wang
 * @since 2024/3/8 16:32
 */
public class KafkaCommonUtil {
    private KafkaCommonUtil() {
        throw new IllegalStateException(UTILITY_CLASS);
    }

    /**
     * 获取record中指定item的值
     *
     * @param record   consumer record
     * @param itemName item name
     * @return item value
     */
    public static String getItemValueFromConsumerRecord(ConsumerRecord<?, ?> record, String itemName) {
        JsonNode jsonNode = PlatFormJsonUtil.deserializeToClassType((String) record.value(), JsonNode.class);

        String result = null;
        try {
            String[] keys = itemName.split("\\.");
            for (int i = 0; i < keys.length; i++) {
                if (i == keys.length - 1) {
                    result = jsonNode.get(keys[i]).asText();
                } else {
                    jsonNode = jsonNode.get(keys[i]);
                }
                if (jsonNode == null) {
                    break;
                }
            }
        } catch (Exception e) {
            // Ignore the error, return null instead
        }
        return result;
    }

    /**
     * 通过topic获取resource type
     *
     * @param topic topic
     * @return resource type
     */
    public static String getResourceTypeFromTopic(String topic) {
        if (StringUtils.isBlank(topic)) {
            return "";
        }

        String[] topicArray = topic.split("_");
        if (topicArray.length < 5) {
            return "";
        }
        return topicArray[2];
    }

    public static double parseDoubleWithDefaultValue(String string, double defaultVal) {
        double value = defaultVal;
        try {
            value = Double.parseDouble(string);
        } catch (Exception e) {
            // Do Nothing, Keep Default Value
        }
        return value;
    }


    /**
     * @param queryStr,  param1=value1;param2=value2
     * @param separator, ;
     * @return
     */
    public static Map<String, String> parse2Map(String queryStr, String separator) {

        Map<String, String> map = new HashMap<>();

        if (queryStr != null && separator != null) {
            String[] querys = queryStr.split(separator);
            for (String query : querys) {
                if (StringUtils.isNotEmpty(query)) {
                    String[] param = query.split("=");
                    if (param.length == 2) {
                        map.put(param[0], param[1]);
                    } else {
                        map.put(param[0], "");
                    }
                }
            }
        }

        return map;
    }

    /**
     * @param queryStr, param1=value1&param2=value2
     * @return
     */
    public static Map<String, String> parse2Map(String queryStr) {
        return parse2Map(queryStr, "&");
    }

    public static String getKafkaHeaderString(Headers headers) {
        if (null == headers) {
            return null;
        }
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        headers.forEach(header ->
                objectNode.put(header.key(), new String(header.value()))
        );
        return objectNode.toString();
    }
}
