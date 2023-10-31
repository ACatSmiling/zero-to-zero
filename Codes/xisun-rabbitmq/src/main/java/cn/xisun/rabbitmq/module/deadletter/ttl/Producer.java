package cn.xisun.rabbitmq.module.deadletter.ttl;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/16 13:35
 * <p>
 * 死信队列，生产者
 */
@Slf4j
public class Producer {

    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);

            // 设置消息的TTL时间，10秒后消息过期
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();

            // 发送10条消息
            for (int i = 1; i < 11; i++) {
                String message = "info" + i;
                channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", properties, message.getBytes());
                log.info("生产者发送消息：{}", message);
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
