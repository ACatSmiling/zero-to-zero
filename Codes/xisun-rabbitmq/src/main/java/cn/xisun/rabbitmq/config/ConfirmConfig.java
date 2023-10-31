package cn.xisun.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author XiSun
 * @since 2023/10/18 10:25
 * <p>
 * 发布确认配置类
 */
// @Configuration
public class ConfirmConfig {

    /**
     * 发布确认交换机
     */
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";

    /**
     * 发布确认队列
     */
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    /**
     * 声明发布确认交换机
     *
     * @return
     */
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    /**
     * 声明发布确认队列
     *
     * @return
     */
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    /**
     * 声明发布确认队列绑定发布确认交换机，routing key为key1
     *
     * @param confirmQueue
     * @param confirmExchange
     * @return
     */
    @Bean
    public Binding queueBinding(@Qualifier("confirmQueue") Queue confirmQueue, @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with("key1");
    }
}
