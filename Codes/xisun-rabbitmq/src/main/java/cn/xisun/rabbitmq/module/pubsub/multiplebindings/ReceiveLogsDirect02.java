package cn.xisun.rabbitmq.module.pubsub.multiplebindings;

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
 * @since 2023/10/15 21:41
 * <p>
 * 多重绑定，消息消费者02
 */
@Slf4j
public class ReceiveLogsDirect02 {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            String queueName = "console";
            channel.queueDeclare(queueName, false, false, false, null);

            // routing key指定为info和warning，只消费普通日志和警告日志
            channel.queueBind(queueName, EXCHANGE_NAME, "info");
            channel.queueBind(queueName, EXCHANGE_NAME, "warning");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                message = "接收绑定键：" + delivery.getEnvelope().getRoutingKey() + "，消息：" + message;
                log.info("错误日志已经接收：[{}]", message);
            };

            log.info("等待接收普通日志和警告日志");
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                log.info("消费失败的tag值：{}", consumerTag);
            });
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
