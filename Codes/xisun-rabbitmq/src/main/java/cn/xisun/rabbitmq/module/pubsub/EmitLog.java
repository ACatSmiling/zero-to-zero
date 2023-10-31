package cn.xisun.rabbitmq.module.pubsub;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/15 15:17
 * <p>
 * 发布/订阅模式，消息生产者
 */
@Slf4j
public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            /*
             * 声明一个exchange
             * 参数1：exchange的名称
             * 参数2：exchange的类型
             */
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.nextLine();
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
                log.info("生产者的消息：{}", message);
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
