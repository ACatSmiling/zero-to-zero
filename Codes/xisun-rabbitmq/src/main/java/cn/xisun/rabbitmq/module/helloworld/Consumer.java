package cn.xisun.rabbitmq.module.helloworld;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/12 22:33
 */
@Slf4j
public class Consumer {

    // 队列名称
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.2.100");
        factory.setUsername("rbmq");
        factory.setPassword("rbmq");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            log.info("等待接收消息....");

            // 推送的消息如何进行消费的接口回调
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
                log.info("消费的消息是：{}", message);
            };

            // 取消消费的一个回调接口，如在消费的时候队列被删除掉了
            CancelCallback cancelCallback = (consumerTag) -> {
                log.info("消息消费被中断");
            };

            /*
             * 消费者消费消息：
             * 参数1：消费哪个队列
             * 参数2：消费成功之后是否要自动应答，true表示自动应答，false表示手动应答
             * 参数3：消费者消费消息成功时的回调
             * 参数4：消费者消费消息失败时的回调
             */
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
