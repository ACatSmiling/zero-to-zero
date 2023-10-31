package cn.xisun.rabbitmq.module.workqueues.confirm;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/13 22:03
 * <p>
 * 批量发布确认
 */
@Slf4j
public class PublishMessageBatch {

    // 发送消息的数量
    private static final Integer MESSAGE_COUNT = 1000;

    public static void publishMessageBatch() {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            // 开启发布确认
            channel.confirmSelect();

            String queueName = "batch_queue";
            channel.queueDeclare(queueName, true, false, false, null);

            // 批量发布确认消息的数量
            int batchSize = 100;
            // 未确认消息个数
            int outstandingMessageCount = 0;

            // 开始时间
            long begin = System.currentTimeMillis();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = i + "";
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                outstandingMessageCount++;
                if (outstandingMessageCount == batchSize) {
                    channel.waitForConfirms();
                    outstandingMessageCount = 0;
                }
            }
            // 为了确保没有剩余的待确认消息，再次确认
            if (outstandingMessageCount > 0) {
                channel.waitForConfirms();
            }
            long end = System.currentTimeMillis();
            log.info("{} 个消息批量发布确认，耗时：{} ms", MESSAGE_COUNT, (end - begin));
        } catch (InterruptedException | IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
