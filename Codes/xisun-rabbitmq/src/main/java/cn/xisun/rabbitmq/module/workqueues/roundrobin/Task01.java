package cn.xisun.rabbitmq.module.workqueues.roundrobin;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/13 11:00
 * <p>
 * 轮询分发消息
 */
@Slf4j
public class Task01 {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // 从控制台当中接受信息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                log.info("消息发送完毕：{}", message);
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
