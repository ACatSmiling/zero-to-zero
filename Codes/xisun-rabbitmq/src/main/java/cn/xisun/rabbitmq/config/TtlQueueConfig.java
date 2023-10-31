package cn.xisun.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XiSun
 * @since 2023/10/16 22:00
 * <p>
 * 延迟队列配置类
 */
@Configuration
public class TtlQueueConfig {

    /**
     * 交换机X
     */
    public static final String X_EXCHANGE = "X";

    /**
     * 队列A
     */
    public static final String QUEUE_A = "QA";

    /**
     * 队列B
     */
    public static final String QUEUE_B = "QB";

    /**
     * 死信交换机Y
     */
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";

    /**
     * 死信队列D
     */
    public static final String DEAD_LETTER_QUEUE = "QD";

    /**
     * 声明交换机X，交换机类型为direct
     *
     * @return
     */
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    /**
     * 声明死信交换机Y，交换机类型为direct
     *
     * @return
     */
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    /**
     * 声明队列QA，ttl为10s，并绑定到死信交换机Y
     *
     * @return
     */
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> args = new HashMap<>(3);
        // 声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        // 声明当前队列绑定死信交换机的routing key
        args.put("x-dead-letter-routing-key", "YD");
        // 声明当前队列的TTL
        args.put("x-message-ttl", 10000);

        return QueueBuilder.durable(QUEUE_A).withArguments(args).build();
    }

    /**
     * 声明队列QA绑定X交换机，routing key为XA
     *
     * @param queueA
     * @param xExchange
     * @return
     */
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    /**
     * 声明队列QB，ttl为40s，并绑定到死信交换机Y
     *
     * @return
     */
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> args = new HashMap<>(3);
        // 声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        // 声明当前队列的死信routing key
        args.put("x-dead-letter-routing-key", "YD");
        // 声明队列的TTL
        args.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(args).build();
    }

    /**
     * 声明队列QB绑定X交换机，routing key为XB
     *
     * @param queueB
     * @param xExchange
     * @return
     */
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    /**
     * 声明死信队列QD
     *
     * @return
     */
    @Bean("queueD")
    public Queue queueD() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    /**
     * 声明死信队列QD绑定Y交换机，routing key为QD
     *
     * @param queueD
     * @param yExchange
     * @return
     */
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD, @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }
}
