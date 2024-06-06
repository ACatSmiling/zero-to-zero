package cn.zero.cloud.platform.kafka.filter;

import cn.zero.cloud.platform.kafka.utils.KafkaCommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * See Details: https://wiki.cisco.com/display/HFWEB/Common+filter+ability+base+on+type+for+message+header+or+message+body
 */
@Configuration("kafkaCommonRecordFilterStrategy")
public class KafkaCommonRecordFilterStrategy {
    private static final Logger logger = LoggerFactory.getLogger(KafkaCommonRecordFilterStrategy.class);
    private static final String NOT_NULL = "not_null";

    @Bean
    @ConfigurationProperties(prefix = "webex.kafka.common.filter.consumer-record.header")
    public Map<String, String> kafkaCommonFilterHeader() {
        return new HashMap<>();
    }

    @Bean
    @ConfigurationProperties(prefix = "webex.kafka.common.filter.consumer-record.payload")
    public Map<String, String> kafkaCommonFilterPayload() {
        return new HashMap<>();
    }

    /**
     * Implementations of this interface can signal that a record about to be delivered to a message listener
     * should be discarded instead of being delivered.
     * true : discarded false: delivery
     *
     * @param headerName
     * @param value
     * @return
     */
    public RecordFilterStrategy<Object, Object> filterRecordByHeaderKey(String headerName, String value) {

        return consumerRecord -> {
            Headers headers = consumerRecord.headers();
            if (headers == null) {
                logger.info("===> Headers is empty, ignore.");
                return true;
            }
            Header header = headers.lastHeader(headerName);

            if (value.equalsIgnoreCase(NOT_NULL)) {
                logger.info("===> Header filter not null, {}.", header == null ? "true" : "false");
                return header == null;
            } else if (header != null) {
                if (!value.equalsIgnoreCase(new String(header.value(), StandardCharsets.UTF_8))) {
                    logger.info("===> Header filter {} - {}, result {} {}. ", headerName, value, new String(header.value(), StandardCharsets.UTF_8), consumerRecord.key());
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * @param queryStr, header1=value1&header2=value2...
     * @return
     */
    public RecordFilterStrategy<Object, Object> filterRecordByMessageHeader(String queryStr) {

        return consumerRecord -> {
            Map<String, String> map = KafkaCommonUtil.parse2Map(queryStr);
            return filterByHeader(consumerRecord, map);
        };
    }

    /**
     * @param queryStr, siteUUID=xxx&userInfo.userID=xxx...
     * @return
     */
    public RecordFilterStrategy<Object, Object> filterRecordByMessagePayload(String queryStr) {

        return consumerRecord -> {
            Map<String, String> map = KafkaCommonUtil.parse2Map(queryStr);
            return filterByPayload(consumerRecord, map);
        };
    }


    public RecordFilterStrategy<Object, Object> filterRecordByMessageHeaderAndPayload() {
        return consumerRecord -> {
            boolean filterHeader = filterByHeader(consumerRecord, kafkaCommonFilterHeader());
            boolean filterPayload = filterByPayload(consumerRecord, kafkaCommonFilterPayload());
            return filterHeader || filterPayload;
        };
    }

    /**
     * @param consumerRecord
     * @param map
     * @return
     * @description return true if one condition matches.
     */
    private static boolean filterByHeader(ConsumerRecord consumerRecord, Map<String, String> map) {
        if (map.isEmpty()) {
            return false;
        }

        Headers headers = consumerRecord.headers();
        if (headers == null) {
            logger.info("===> Headers is empty, ignore.");
            return true;
        }

        boolean isFilter = false;
        for (String key : map.keySet()) {
            Header header = headers.lastHeader(key);
            if (header != null && map.get(key).equalsIgnoreCase(new String(header.value(), StandardCharsets.UTF_8))) {
                isFilter = true;
                break;
            }
        }
        if (isFilter) {
            logger.info("filter consume record by header, recordKey = {}, filter = {}", consumerRecord.key(), map);
        }
        return isFilter;
    }

    /**
     * @param consumerRecord
     * @param map
     * @return
     * @description return true if one condition matches.
     */
    private static boolean filterByPayload(ConsumerRecord consumerRecord, Map<String, String> map) {
        if (map.isEmpty()) {
            return false;
        }
        boolean isFilter = false;

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String itemValueFromConsumerRecord = KafkaCommonUtil.getItemValueFromConsumerRecord(consumerRecord, key);
            if (StringUtils.isNotEmpty(itemValueFromConsumerRecord) && !"null".equalsIgnoreCase(itemValueFromConsumerRecord)) {
                String[] filterValues = value.split(",");
                for (String filterValue : filterValues) {
                    if (filterValue.equalsIgnoreCase(itemValueFromConsumerRecord)) {
                        isFilter = true;
                        break;
                    }
                }
                if (isFilter) {
                    break;
                }
            }
        }
        if (isFilter) {
            logger.info("filter consume record by payload, recordKey = {}, filter = {}", consumerRecord.key(), map);
        }
        return isFilter;
    }

}
