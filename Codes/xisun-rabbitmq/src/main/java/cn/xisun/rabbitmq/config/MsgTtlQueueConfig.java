package cn.xisun.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XiSun
 * @since 2023/10/17 10:04
 * <p>
 * 延迟队列配置类
 */
@Configuration
public class MsgTtlQueueConfig {

    /**
     * 死信交换机Y
     */
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";

    /**
     * 队列QC
     */
    public static final String QUEUE_C = "QC";

    /**
     * 声明队列QC，不设置ttl，并绑定到死信交换机Y
     *
     * @return
     */
    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> args = new HashMap<>(3);
        // 声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        // 声明当前队列绑定死信交换机的routing key
        args.put("x-dead-letter-routing-key", "YD");
        // 不声明当前队列的TTL
        return QueueBuilder.durable(QUEUE_C).withArguments(args).build();
    }

    /**
     * 声明队列QC绑定X交换机，routing key为XC
     *
     * @param queueC
     * @param xExchange
     * @return
     */
    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }
}
