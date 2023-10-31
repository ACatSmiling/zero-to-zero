package cn.xisun.rabbitmq.module.workqueues.prefetch;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/13 13:41
 * <p>
 * 预取值
 */
@Slf4j
public class Task03 {

    private final static String ACK_QUEUE_NAME = "prefetch_queue";

    public static void main(String[] args) {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            // 设置队列持久化
            boolean durable = true;
            channel.queueDeclare(ACK_QUEUE_NAME, durable, false, false, null);

            // 从控制台当中接受信息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                // 当durable为true的时候，添加MessageProperties.PERSISTENT_TEXT_PLAIN参数，设置消息持久化
                channel.basicPublish("", ACK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
                log.info("消息发送完毕：{}", message);
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
