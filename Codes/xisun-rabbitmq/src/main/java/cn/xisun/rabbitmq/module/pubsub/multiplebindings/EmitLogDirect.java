package cn.xisun.rabbitmq.module.pubsub.multiplebindings;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/15 21:36
 * <p>
 * 多重绑定，消息生产者
 */
@Slf4j
public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            // 声明一个direct类型的exchange
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            // 创建多个bindingKey，发送多条日志消息
            Map<String, String> bindingKeyMap = new HashMap<>();
            bindingKeyMap.put("debug", "调试信息");
            bindingKeyMap.put("info", "普通信息");
            bindingKeyMap.put("warning", "警告信息");
            bindingKeyMap.put("error", "错误信息");

            for (Map.Entry<String, String> bindingKeyEntry : bindingKeyMap.entrySet()) {
                String bindingKey = bindingKeyEntry.getKey();
                String message = bindingKeyEntry.getValue();
                channel.basicPublish(EXCHANGE_NAME, bindingKey, null, message.getBytes(StandardCharsets.UTF_8));
                log.info("生产者的消息：{}", message);
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
