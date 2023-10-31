package cn.xisun.rabbitmq.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author XiSun
 * @since 2023/10/18 13:58
 * <p>
 * 备份交换机配置类
 */
@Slf4j
@Configuration
public class BackupExchangeConfig {

    /**
     * 发布确认交换机
     */
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";

    /**
     * 发布确认队列
     */
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    /**
     * 备份交换机
     */
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";

    /**
     * 备份队列
     */
    public static final String BACKUP_QUEUE_NAME = "backup.queue";

    /**
     * 报警队列
     */
    public static final String WARNING_QUEUE_NAME = "warning.queue";

    /**
     * 声明备份交换机，类型是fanout
     *
     * @return
     */
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    /**
     * 声明备份队列
     *
     * @return
     */
    @Bean("backQueue")
    public Queue backQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    /**
     * 声明警告队列
     *
     * @return
     */
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    /**
     * 声明备份队列的绑定关系
     *
     * @param backQueue
     * @param backupExchange
     * @return
     */
    @Bean
    public Binding backupQueueBinding(@Qualifier("backQueue") Queue backQueue,
                                      @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(backQueue).to(backupExchange);
    }

    /**
     * 声明报警队列的绑定关系
     *
     * @param warningQueue
     * @param backupExchange
     * @return
     */
    @Bean
    public Binding warningQueueBinding(@Qualifier("warningQueue") Queue warningQueue,
                                       @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }

    /**
     * 声明发布确认交换机，并绑定备份交换机
     *
     * @return
     */
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        ExchangeBuilder exchangeBuilder = ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                // 设置发布确认交换机的备份交换机
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME);
        return exchangeBuilder.build();
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
    public Binding confirmQueueBinding(@Qualifier("confirmQueue") Queue confirmQueue,
                                       @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with("key1");
    }
}