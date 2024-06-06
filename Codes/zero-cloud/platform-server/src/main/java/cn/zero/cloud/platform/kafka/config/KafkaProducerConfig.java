package cn.zero.cloud.platform.kafka.config;

import cn.zero.cloud.platform.kafka.common.message.BaseMessage;
import cn.zero.cloud.platform.kafka.common.pojo.notification.KafkaNotificationTopics;
import cn.zero.cloud.platform.kafka.producer.notification.KafkaNotificationProducerRetryListener;
import cn.zero.cloud.platform.kafka.producer.notification.KafkaNotificationSender;
import cn.zero.cloud.platform.kafka.producer.resending.client.Resender;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Xisun Wang
 * @since 2024/3/6 14:25
 */
@Configuration
public class KafkaProducerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerConfig.class);

    private final KafkaProperties kafkaProperties;

    private final KafkaParameterConfig kafkaParameterConfig;

    @Autowired
    public KafkaProducerConfig(KafkaProperties kafkaProperties, KafkaParameterConfig kafkaParameterConfig) {
        this.kafkaProperties = kafkaProperties;
        this.kafkaParameterConfig = kafkaParameterConfig;
    }

    /**
     * 注入KafkaNotificationTopics bean
     *
     * @return KafkaNotificationTopics
     */
    @Bean
    public KafkaNotificationTopics kafkaNotificationTopics() {
        KafkaNotificationTopics kafkaNotificationTopics = new KafkaNotificationTopics();
        kafkaNotificationTopics.setSummaryTopic(kafkaParameterConfig.getSummaryNotificationTopic());
        kafkaNotificationTopics.setSummaryTranscriptTopic(kafkaParameterConfig.getSummaryTranscriptNotificationTopic());
        kafkaNotificationTopics.setSuiteRecordingTopic(kafkaParameterConfig.getSuiteRecordingNotificationTopic());
        return kafkaNotificationTopics;
    }

    @Bean
    public KafkaTemplate<Object, BaseMessage<Object>> kafkaNotificationTemplate() {
        ProducerFactory<Object, BaseMessage<Object>> factory = new DefaultKafkaProducerFactory<>(getProducerProperties(false));
        KafkaTemplate<Object, BaseMessage<Object>> kafkaTemplate = new KafkaTemplate<>(factory);
        KafkaNotificationProducerRetryListener<Object, BaseMessage<Object>> kafkaNotificationProducerRetryListener = new KafkaNotificationProducerRetryListener<>();
        kafkaNotificationProducerRetryListener.setEnableRetry(kafkaParameterConfig.retryConfigItems().isEnable());
        kafkaNotificationProducerRetryListener.setResender(new Resender.Default(kafkaParameterConfig.retryConfigItems().getWnsVip(), kafkaParameterConfig.retryConfigItems().getAppToken()));
        kafkaTemplate.setProducerListener(kafkaNotificationProducerRetryListener);
        LOGGER.info("kafka config kafkaNotificationSender, enable={} , wnsVip={}", kafkaParameterConfig.retryConfigItems().isEnable(), kafkaParameterConfig.retryConfigItems().getWnsVip());
        return kafkaTemplate;
    }

    @Bean
    public KafkaTemplate<Object, BaseMessage<Object>> kafkaNotificationCompressionTemplate() {
        ProducerFactory<Object, BaseMessage<Object>> factory = new DefaultKafkaProducerFactory<>(getProducerProperties(true));
        KafkaTemplate<Object, BaseMessage<Object>> kafkaTemplate = new KafkaTemplate<>(factory);
        KafkaNotificationProducerRetryListener<Object, BaseMessage<Object>> kafkaNotificationProducerRetryListener = new KafkaNotificationProducerRetryListener<>();
        kafkaNotificationProducerRetryListener.setEnableRetry(kafkaParameterConfig.retryConfigItems().isEnable());
        kafkaNotificationProducerRetryListener.setResender(new Resender.Default(kafkaParameterConfig.retryConfigItems().getWnsVip(), kafkaParameterConfig.retryConfigItems().getAppToken()));
        kafkaTemplate.setProducerListener(kafkaNotificationProducerRetryListener);
        LOGGER.info("kafka config kafkaNotificationSender, enable={} , wnsVip={}", kafkaParameterConfig.retryConfigItems().isEnable(), kafkaParameterConfig.retryConfigItems().getWnsVip());
        return kafkaTemplate;
    }

    /**
     * 注入一个KafkaNotificationSender
     *
     * @return KafkaNotificationSender
     */
    @Bean
    public KafkaNotificationSender kafkaNotificationSender() {
        KafkaNotificationSender kafkaNotificationSender = new KafkaNotificationSender();
        Map<String, Object> clientStateProperties = new HashMap<>();
        clientStateProperties.put("enabled", kafkaParameterConfig.enabled());
        clientStateProperties.put("servers", String.join("", kafkaProperties.getProducer().getBootstrapServers()));
        Map<String, String> rateLimiterMap = kafkaParameterConfig.senderRatelimiterMap();
        if (!CollectionUtils.isEmpty(rateLimiterMap)) {
            clientStateProperties.put("ratelimitermap", rateLimiterMap);
        }
        kafkaNotificationSender.initiateKafkaClientState(clientStateProperties);
        kafkaNotificationSender.setEnableRetry(kafkaParameterConfig.retryConfigItems().isEnable());
        kafkaNotificationSender.setResender(new Resender.Default(kafkaParameterConfig.retryConfigItems().getWnsVip(), kafkaParameterConfig.retryConfigItems().getAppToken()));
        return kafkaNotificationSender;
    }

    /**
     * 构建producer配置
     *
     * @param enableCompression 是否启用压缩
     * @return producer properties
     */
    private Map<String, Object> getProducerProperties(boolean enableCompression) {
        Map<String, Object> props = kafkaProperties.buildProducerProperties(null);
        if (enableCompression) {
            // 生产者发送的消息将使用GZIP压缩
            props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
            // 生产者发送请求的最大字节数
            props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "10505760");
            // 生产者用于缓冲等待发送到服务器的消息的内存大小
            props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "10505760");
        }
        // 检查安全配置的协议是否等于字符串"SASL_SSL"
        // "SASL_SSL"是Kafka支持的一种安全协议，它结合了SASL(简单认证和安全层)和SSL(安全套接字层)来提供认证和加密功能
        if (kafkaProperties.getSecurity() != null && "SASL_SSL".equals(kafkaProperties.getSecurity().getProtocol())) {
            // 禁用生产者的幂等性特性
            // 幂等性在Kafka中意味着即使生产者发送了重复的消息，也只有一条会被实际写入Kafka主题
            props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
            if (isDefaultTSL(kafkaProperties.getSsl().getTrustStoreLocation())) {
                // 若未定义spring.kafka.ssl.truststore.location，则构建默认的TrustStore文件的路径
                String truststoreLocation = System.getProperty("java.home") + File.separator + "lib"
                        + File.separator + "security" + File.separator + "cacerts";
                // 设置ssl.truststore.location
                props.put("ssl.truststore.location", truststoreLocation);
            }
        }
        // change from 300000 to 600000 reduce producerMetaData expire and fetch
        // 生产者缓存的元数据信息最大年龄为600000毫秒 (10分钟)，超过这个时间，生产者将强制刷新其元数据信息
        props.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, 600000);
        // 如果生产者缓存的元数据没有在600000毫秒 (10分钟) 内被使用，生产者将丢弃该元数据
        props.put(ProducerConfig.METADATA_MAX_IDLE_CONFIG, 600000);
        return props;
    }

    private boolean isDefaultTSL(Resource trustStoreLocation) {
        return trustStoreLocation == null;
    }
}
