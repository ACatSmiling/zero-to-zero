package cn.xisun.rabbitmq.module.deadletter.reject;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/16 13:38
 * <p>
 * 死信队列，普通的消费者
 */
@Slf4j
public class Consumer01 {

    // 普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    // 死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();

            // 声明死信和普通交换机，类型为direct
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

            // 声明死信队列
            String deadQueue = "dead-queue";
            channel.queueDeclare(deadQueue, false, false, false, null);
            // 死信队列绑定的死信交换机与routing key
            channel.queueBind(deadQueue, DEAD_EXCHANGE, "lisi");

            // 正常队列绑定死信队列信息
            Map<String, Object> params = new HashMap<>();
            // 正常队列设置死信交换机，key是固定值x-dead-letter-exchange
            params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
            // 正常队列设置死信routing key，key是固定值x-dead-letter-routing-key
            params.put("x-dead-letter-routing-key", "lisi");

            // 声明普通队列，添加绑定死信队列的参数
            String normalQueue = "normal-queue";
            channel.queueDeclare(normalQueue, false, false, false, params);
            channel.queueBind(normalQueue, NORMAL_EXCHANGE, "zhangsan");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                if ("info5".equals(message)) {
                    log.info("Consumer01接收到但是拒绝消费的消息：{}", message);
                    // 参数requeue设置为false，代表拒绝该消息重新入队，该队列如果配置了死信交换机将发送到死信队列中
                    channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
                } else {
                    log.info("Consumer01正常消费的消息：{}", message);
                    // 手动应答
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };

            log.info("等待接收消息");
            boolean autoAck = false;
            channel.basicConsume(normalQueue, autoAck, deliverCallback, consumerTag -> {
            });
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
