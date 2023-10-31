package cn.xisun.rabbitmq.module.topic;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XiSun
 * @since 2023/10/16 9:49
 * <p>
 * 主题模式，消息生产者
 */
@Slf4j
public class EmitLogTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            // 声明一个topic类型的exchange
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            /*
             * Q1 ---> 绑定的是中间为orange带3个单词的字符串(*.orange.*)
             * Q2 ---> 绑定的是最后一个单词是rabbit的3个单词的字符串(*.*.rabbit)，以及第一个单词是lazy的多个单词的字符串(lazy.#)
             */
            Map<String, String> bindingKeyMap = new HashMap<>();
            bindingKeyMap.put("quick.orange.rabbit", "被队列 Q1 和 Q2 接收到");
            bindingKeyMap.put("lazy.orange.elephant", "被队列 Q1 和 Q2 接收到");
            bindingKeyMap.put("quick.orange.fox", "被队列 Q1 接收到");
            bindingKeyMap.put("lazy.brown.fox", "被队列 Q2 接收到");
            bindingKeyMap.put("lazy.pink.rabbit", "虽然满足两个绑定规则，但只被队列 Q2 接收一次");
            bindingKeyMap.put("quick.brown.fox", "未匹配任何绑定规则，不会被任何队列接收到，消息会被丢弃");
            bindingKeyMap.put("quick.orange.male.rabbit", "四个单词，不匹配 Q1 的绑定规则，消息会被丢弃");
            bindingKeyMap.put("lazy.orange.male.rabbit", "四个单词，匹配 Q2 的绑定规则，被 Q2 接收到");

            for (Map.Entry<String, String> bindingKeyEntry : bindingKeyMap.entrySet()) {
                String bindingKey = bindingKeyEntry.getKey();
                String message = bindingKeyEntry.getValue();
                channel.basicPublish(EXCHANGE_NAME, bindingKey, null, message.getBytes(StandardCharsets.UTF_8));
                log.info("生产者的消息：{}", message);
            }
        }
    }
}
