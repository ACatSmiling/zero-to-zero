package cn.zero.cloud.platform.kafka.utils;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.core.io.Resource;
import org.springframework.util.unit.DataSize;

import java.time.Duration;
import java.util.Optional;

/**
 * Kafka Consumer配置策略检查工具类
 *
 * @author Xisun Wang
 * @since 2024/3/7 15:46
 */
public class KafkaConsumerConfigUtil {
    /**
     * 消费者在单次调用poll()方法时，能够获取的最大记录数
     */
    private static final int MAX_POLL_RECORDS_CONFIG = 5;

    /**
     * 消费者从每个分区里面在单次调用poll()方法时，能够拉取的数据的最大字节数
     */
    private static final int MAX_PARTITION_FETCH_BYTES_CONFIG = ConsumerConfig.DEFAULT_MAX_PARTITION_FETCH_BYTES;

    /**
     * 消费者是否自动提交其偏移量offsets
     */
    private static final boolean ENABLE_AUTO_COMMIT_CONFIG = false;

    /**
     * 消费者在两次连续调用poll()方法之间的最大时间间隔，毫秒值
     * 如果超过这个时间间隔没有进行poll()操作，消费者将被认为死亡(dead)，并且它的分区将被重新分配给其他消费者
     */
    private static final int MAX_POLL_INTERVAL_MS_CONFIG = 300000;

    /**
     * 消费者在没有初始偏移量，或者当前的偏移量无效(比如该偏移量已经不在服务器上了，可能是因为该数据已经被删除)时该如何处理：
     * earliest：自动重置偏移量为最小偏移量，消费者将从分区中可用的最早记录开始读取数据
     * latest：自动重置偏移量为最新偏移量，消费者将从新产生的记录开始读取数据，跳过旧的记录
     * none：如果没有找到消费者组的先前偏移量，则向消费者抛出异常，应用应该处理这个异常
     */
    private static final String AUTO_OFFSET_RESET = "latest";

    /**
     * 消费者在发送fetch请求到Kafka broker时，等待broker返回数据的最大时间
     * 即broker在返回fetch响应之前等待更多消息到达的最长时间，以便尽可能填充到MAX_PARTITION_FETCH_BYTES_CONFIG指定的最大数据量
     */
    private static final int FETCH_MAX_WAIT_MS_CONFIG = 500;

    /**
     * 消费者从 Kafka broker 发起 fetch 请求时，broker 返回数据前所需积累的最小数据量
     */
    private static final int FETCH_MIN_BYTES_CONFIG = 1;

    private static final String KEY_DESERIALIZER = "class org.apache.kafka.common.serialization.StringDeserializer";

    private static final String VALUE_DESERIALIZER = "class org.apache.kafka.common.serialization.StringDeserializer";

    /**
     * 1. 如果maxPollRecordsConfig包含一个值，并且这个值等于MAX_POLL_RECORDS_CONFIG，那么map()会返回Optional<Boolean>，其中包含true
     * 2. 如果maxPollRecordsConfig包含一个值，但这个值不等于MAX_POLL_RECORDS_CONFIG，那么map()会返回Optional<Boolean>，其中包含false
     * 3. 如果maxPollRecordsConfig为空，则orElse(true)会被触发，方法返回true
     *
     * @param maxPollRecordsConfig maxPollRecordsConfig
     * @return 当且仅当maxPollRecordsConfig不为空，且值不等于MAX_POLL_RECORDS_CONFIG时，返回false
     */
    public static boolean isDefaultMPRC(Optional<Integer> maxPollRecordsConfig) {
        return maxPollRecordsConfig.map(integer -> integer == MAX_POLL_RECORDS_CONFIG).orElse(true);
    }

    public static boolean isDefaultMPRC2(Integer maxPollRecordsConfig) {
        Optional<Integer> optional = Optional.ofNullable(maxPollRecordsConfig);
        return optional.map(integer -> integer == MAX_POLL_RECORDS_CONFIG).orElse(true);
    }

    public static boolean isDefaultMPFBC(Optional<String> maxPartitionFetchBytesConfig) {
        return maxPartitionFetchBytesConfig.map(s -> Integer.parseInt(s) == MAX_PARTITION_FETCH_BYTES_CONFIG).orElse(true);
    }

    public static boolean isDefaultACC(Optional<Boolean> auto_commit_config) {
        //false,false 0 ; true false 1
        return auto_commit_config.map(aBoolean -> Boolean.compare(aBoolean, ENABLE_AUTO_COMMIT_CONFIG) == 0).orElse(true);
        //null or false as default value
    }

    public static boolean isDefaultMPIMC(Optional<String> max_poll_interval_ms_config) {
        if (max_poll_interval_ms_config.isPresent()) {
            return Integer.valueOf(max_poll_interval_ms_config.get()) == MAX_POLL_INTERVAL_MS_CONFIG;
        }
        return true;
    }

    public static boolean isDefaultAOR(Optional<String> auto_offset_reset) {
        if (auto_offset_reset.isPresent()) {
            return auto_offset_reset.get().equalsIgnoreCase(AUTO_OFFSET_RESET);
        }
        return true;
    }

    public static boolean isDefaultKD(Optional<String> key_deserializer) {
        return key_deserializer.map(s -> s.equalsIgnoreCase(KEY_DESERIALIZER)).orElse(true);
    }

    public static boolean isDefaultVD(Optional<String> value_deserizlizer) {
        if (value_deserizlizer.isPresent()) {
            return value_deserizlizer.get().equalsIgnoreCase(VALUE_DESERIALIZER);
        }
        return true;
    }

    public static boolean isDefaultVD2(String value_deserizlizer) {
        return value_deserizlizer.equalsIgnoreCase(VALUE_DESERIALIZER);
    }

    public static boolean isDefaultTSL(Resource trustStoreLocation) {
        return trustStoreLocation == null;
    }

    public static boolean isDefaultFMW(Optional<Duration> fetchMaxWaitMSConfig) {
        if (fetchMaxWaitMSConfig.isPresent()) {
            return fetchMaxWaitMSConfig.get().getNano() == FETCH_MAX_WAIT_MS_CONFIG * 1000000;
        }
        return true;
    }

    public static boolean isDefaultFMB(Optional<DataSize> fetchMinBytesConfig) {
        if (fetchMinBytesConfig.isPresent()) {
            return fetchMinBytesConfig.get().toBytes() == FETCH_MIN_BYTES_CONFIG;
        }
        return true;
    }

}
