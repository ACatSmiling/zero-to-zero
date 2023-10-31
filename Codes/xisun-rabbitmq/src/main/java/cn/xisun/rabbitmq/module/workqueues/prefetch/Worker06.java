package cn.xisun.rabbitmq.module.workqueues.prefetch;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import cn.xisun.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/13 13:43
 */
@Slf4j
public class Worker06 {

    private static final String ACK_QUEUE_NAME = "prefetch_queue";

    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();

            // 设置预取值为5
            int prefetchCount = 5;
            channel.basicQos(prefetchCount);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
                log.info("消费的消息是：{}", message);
                SleepUtils.sleep(20);
                log.info("消息处理时间很慢");
                /*
                 * 参数1：消息标记，tag
                 * 参数2：是否批量应答未应答消息
                 */
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };

            CancelCallback cancelCallback = (consumerTag) -> {
                log.info("消息消费被中断");
            };

            log.info("Worker04 消费者启动等待消费......");
            // 采用手动应答
            boolean autoAck = false;
            channel.basicConsume(ACK_QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
