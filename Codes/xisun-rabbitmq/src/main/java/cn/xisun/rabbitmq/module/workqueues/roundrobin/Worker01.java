package cn.xisun.rabbitmq.module.workqueues.roundrobin;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/13 10:50
 */
@Slf4j
public class Worker01 {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
                log.info("消费的消息是：{}", message);
            };

            CancelCallback cancelCallback = (consumerTag) -> {
                log.info("消息消费被中断");
            };

            log.info("Worker01 消费者启动等待消费......");
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
