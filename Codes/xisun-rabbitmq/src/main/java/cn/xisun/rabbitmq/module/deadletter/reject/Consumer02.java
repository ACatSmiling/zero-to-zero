package cn.xisun.rabbitmq.module.deadletter.reject;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/16 14:36
 * <p>
 * 死信队列，消费死信消息的消费者
 */
@Slf4j
public class Consumer02 {

    private static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] argv) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

            String deadQueue = "dead-queue";
            channel.queueDeclare(deadQueue, false, false, false, null);
            channel.queueBind(deadQueue, DEAD_EXCHANGE, "lisi");

            log.info("等待接收死信队列消息");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                log.info("Consumer02 接收死信队列的消息：{}", message);
            };
            channel.basicConsume(deadQueue, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
