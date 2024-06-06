package cn.zero.cloud.platform.kafka.config;

import cn.zero.cloud.platform.utils.PlatFormJsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Xisun Wang
 * @since 4/28/2024 10:26
 */
@Component
public class KafkaConfigPrinterConfig implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConfigPrinterConfig.class);

    private final KafkaProperties kafkaProperties;

    public KafkaConfigPrinterConfig(@Autowired KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 打印消费者配置
        Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties(null);
        LOGGER.info("Kafka Consumer Configurations: {}", PlatFormJsonUtil.serializeToJson(consumerProperties));

        // 打印生产者配置
        Map<String, Object> producerProperties = kafkaProperties.buildProducerProperties(null);
        LOGGER.info("Kafka Producer Configurations: {}", PlatFormJsonUtil.serializeToJson(producerProperties));
    }
}
