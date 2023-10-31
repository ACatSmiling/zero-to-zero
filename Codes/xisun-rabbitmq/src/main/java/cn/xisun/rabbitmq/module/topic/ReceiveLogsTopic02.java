package cn.xisun.rabbitmq.module.topic;

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
 * @since 2023/10/16 9:58
 * <p>
 * 主题模式，消息消费者02
 */
@Slf4j
public class ReceiveLogsTopic02 {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            // 声明Q2队列与绑定关系
            String queueName = "Q2";
            channel.queueDeclare(queueName, false, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
            channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                message = "接收队列：" + queueName + "，接收绑定键：" + delivery.getEnvelope().getRoutingKey() + "，消息：" + message;
                log.info("消息已经接收：[{}]", message);
            };

            log.info("等待接收消息");
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                log.info("消费失败的tag值：{}", consumerTag);
            });
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
