package cn.xisun.rabbitmq.module.workqueues.confirm;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/13 22:02
 * <p>
 * 单个发布确认
 */
@Slf4j
public class PublishMessageIndividually {

    // 发送消息的数量
    private static final Integer MESSAGE_COUNT = 1000;

    public static void publishMessageIndividually() {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            // 开启发布确认
            channel.confirmSelect();

            String queueName = "individually_queue";
            channel.queueDeclare(queueName, true, false, false, null);

            // 开始时间
            long begin = System.currentTimeMillis();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = i + "";
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                // 服务端返回false或超时时间内未返回，生产者可以消息重发
                channel.waitForConfirms();
            }
            // 结束时间
            long end = System.currentTimeMillis();

            log.info("{} 个消息单独发布确认，耗时：{} ms", MESSAGE_COUNT, (end - begin));
        } catch (IOException | InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
