package cn.xisun.rabbitmq.priority;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author XiSun
 * @since 2023/10/18 15:59
 * <p>
 * 优先级，消息生产者
 */
@Slf4j
public class Producer {

    private static final String QUEUE_NAME = "priority_queue";

    public static void main(String[] args) throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel();) {
            // 给消息赋予一个priority属性
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();

            for (int i = 1; i < 11; i++) {
                String message = "info" + i;
                if (i == 5) {
                    channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
                } else {
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                }
                log.info("发送消息：{}", message);
            }
        }
    }
}
