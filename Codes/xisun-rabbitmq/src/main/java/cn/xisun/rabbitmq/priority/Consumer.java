package cn.xisun.rabbitmq.priority;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/18 15:59
 * <p>
 * 优先级，消息消费者
 */
@Slf4j
public class Consumer {

    private static final String QUEUE_NAME = "priority_queue";

    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();

            // 设置队列的最大优先级，最大可以设置到255，官网推荐1~10，如果设置太高比较吃内存和CPU
            Map<String, Object> params = new HashMap<>();
            params.put("x-max-priority", 10);
            channel.queueDeclare(QUEUE_NAME, true, false, false, params);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String receivedMessage = new String(delivery.getBody());
                log.info("接收到消息：{}", receivedMessage);
            };

            log.info("消费者等待消费");
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, (consumerTag) -> {
                log.info("消费者无法消费消息时调用，如队列被删除");
            });
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
