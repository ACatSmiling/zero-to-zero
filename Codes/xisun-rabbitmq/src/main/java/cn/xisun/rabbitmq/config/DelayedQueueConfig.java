package cn.xisun.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XiSun
 * @since 2023/10/17 12:21
 * <p>
 * 延迟队列插件配置类
 */
@Configuration
public class DelayedQueueConfig {

    /**
     * 延迟交换机
     */
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";

    /**
     * 延迟队列
     */
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";

    /**
     * 延迟routing key
     */
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    /**
     * 自定义延迟交换机，交换机类型是direct
     *
     * @return
     */
    @Bean("delayedExchange")
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        // 自定义交换机的类型
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    /**
     * 声明延迟队列
     *
     * @return
     */
    @Bean("delayedQueue")
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    /**
     * 声明延迟队列绑定延迟交换机，routing key为延迟routing key
     *
     * @param delayedQueue
     * @param delayedExchange
     * @return
     */
    @Bean
    public Binding bindingDelayedQueue(@Qualifier("delayedQueue") Queue delayedQueue, @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
