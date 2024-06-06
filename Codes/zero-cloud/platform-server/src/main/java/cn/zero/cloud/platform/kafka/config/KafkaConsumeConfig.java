package cn.zero.cloud.platform.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.io.File;
import java.time.Duration;
import java.util.Map;

/**
 * @author Xisun Wang
 * @since 2024/3/6 14:25
 */
@Configuration
@EnableKafka // 启用Kafka监听器容器的配置，如果使用@KafkaListener注解来定义消费者方法，应显式地添加@EnableKafka注解
public class KafkaConsumeConfig {
    private final KafkaProperties kafkaProperties;

    @Autowired
    public KafkaConsumeConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * 注入一个ConcurrentKafkaListenerContainerFactory的Bean，用于创建能够处理String类型的Key和Value的Kafka消息的监听器
     * 该工厂配置为单线程、批量监听、手动立即确认消息，并且在授权异常发生时会尝试重试
     *
     * @return ConcurrentKafkaListenerContainerFactory<String, String>
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> rateLimitConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // 设置消费者工厂，负责创建Kafka消费者实例
        factory.setConsumerFactory(rateLimitConsumerFactory());
        // 设置创建可以处理批量消息的监听器，在批量监听模式下，Kafka监听器可以一次接收多条消息作为一个列表，而不是一次处理单条消息
        factory.setBatchListener(true);
        // 设置容器的确认模式(acknowledgment mode)为MANUAL_IMMEDIATE，这意味着消费者将手动确认消息，并且确认将立即发生
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        // 设置授权异常重试间隔，当监听器容器捕获到认证相关的异常时，会等待1分钟后重试
        factory.getContainerProperties().setAuthExceptionRetryInterval(Duration.ofMinutes(1));
        // 设置并发数为1，即每个监听器容器将只有一个消费者线程来处理消息
        factory.setConcurrency(1);
        // 设置ackDiscarded属性为true，它指示容器在监听器因异常而丢弃消息时也发送ack(确认)，这使得消息因为某些原因无法被处理时，它不会被重新发送
        // factory.setAckDiscarded(true);
        return factory;
    }

    /**
     * 注入一个创建Kafka消费者的工厂
     *
     * @return ConsumerFactory<String, String>
     */
    @Bean
    public ConsumerFactory<String, String> rateLimitConsumerFactory() {
        // 调用kafkaProperties对象的buildConsumerProperties方法，构建并返回一个包含Kafka消费者默认配置的Map对象
        Map<String, Object> props = kafkaProperties.buildConsumerProperties(null);
        // 设置Kafka集群的地址到消费者配置中
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getConsumer().getBootstrapServers());
        // 禁用自动提交偏移量(offset)的功能，这意味着消费者需要手动提交它们读取的消息的偏移量
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // 设置当没有有效的偏移量时，消费者应该从哪里开始读取数据。LATEST策略意味着消费者将从新产生的记录开始读取数据，即最新的记录
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.LATEST.name().toLowerCase());
        // 设置消息的key的反序列化器
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 设置消息的value的反序列化器
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 设置消费者的最大poll(拉取)间隔时间，这里设置为1小时（3600000毫秒）
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 3600000);
        // 设置每次调用poll操作能够返回的最大记录数，这里设置为100。这是速率限制的一部分，用来控制每次拉取的消息数量
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        // 设置服务器在单次fetch请求中返回给消费者的最小数据量，这里设置为10MB(10485776字节)
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 10485776);
        // 设置消费者在单次fetch请求中等待服务器返回所需数据量的最大时间，这里设置为5000毫秒(5秒)
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 5000);
        // 设置消费者会话的超时时间，这里设置为10000毫秒(10秒)。会话超时时间定义了消费者在被认为死亡之前可以与Kafka集群断开连接的最长时间
        // 如果在这个超时时间内，消费者没有向Kafka集群发送心跳信号，Kafka就会认为该消费者已经死亡，会触发rebalance操作，将该消费者所消费的分区分配给其他消费者
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new StringDeserializer());
    }
}
