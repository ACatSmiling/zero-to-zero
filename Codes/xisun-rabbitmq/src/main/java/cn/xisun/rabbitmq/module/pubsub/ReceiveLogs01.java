package cn.xisun.rabbitmq.module.pubsub;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/15 15:26
 * <p>
 * 发布/订阅模式，消息消费者01
 */
@Slf4j
public class ReceiveLogs01 {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) {
        try {
            // 不要放到try上，否则还没等到消费消息，channel就关闭了
            Channel channel = RabbitMqUtils.getChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            /*
             * 生成一个临时的队列，队列的名称是随机的，
             * 当消费者断开和该队列的连接时，临时队列会自动删除
             */
            String queueName = channel.queueDeclare().getQueue();
            log.info("临时队列名称：{}", queueName);

            // 把临时队列绑定发布者的exchange，其中，routing key(也称之为binding key)为空字符串，发布/订阅模式会忽略routing key
            channel.queueBind(queueName, EXCHANGE_NAME, "");

            // 声明一个消息处理的回调函数
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                log.info("控制台打印数据成功：{}", message);
            };

            log.info("等待接收消息，把接收到的消息打印在控制台.....");
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                log.info("消费失败的tag值：{}", consumerTag);
            });
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
