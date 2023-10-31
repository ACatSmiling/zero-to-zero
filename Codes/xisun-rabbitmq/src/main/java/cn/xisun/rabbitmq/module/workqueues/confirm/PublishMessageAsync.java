package cn.xisun.rabbitmq.module.workqueues.confirm;

import cn.xisun.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/13 22:04
 * <p>
 * 异步发布确认
 */
@Slf4j
public class PublishMessageAsync {

    // 发送消息的数量
    private static final Integer MESSAGE_COUNT = 1000;

    public static void publishMessageAsync() {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            // 开启发布确认
            channel.confirmSelect();

            String queueName = "async_queue";
            channel.queueDeclare(queueName, true, false, false, null);

            /*
             * 线程安全有序的一个哈希表，适用于高并发的情况
             * 1.轻松的将序号与消息进行关联
             * 2.轻松批量删除条目 只要给到序列号
             * 3.支持并发访问
             */
            ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

            /*
             * 确认收到消息的一个回调
             * 参数1：消息序列号
             * 参数2： true表示可以确认小于等于当前序列号的消息，false表示只确认当前序列号消息
             */
            ConfirmCallback ackCallback = (sequenceNumber, multiple) -> {
                if (multiple) {
                    // 返回的是小于等于当前序列号的未确认消息，是一个Map
                    ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(sequenceNumber, true);
                    // 清除该部分未确认消息（已被成功确认）
                    confirmed.clear();
                } else {
                    // 只清除当前序列号的消息
                    outstandingConfirms.remove(sequenceNumber);
                }
            };

            ConfirmCallback nackCallback = (sequenceNumber, multiple) -> {
                String message = outstandingConfirms.get(sequenceNumber);
                log.info("发布的消息未被确认：{}，序列号是：{}", message, sequenceNumber);
            };

            /*
             * 添加一个异步发布确认的监听器
             * 参数1：确认收到消息的回调
             * 参数2：未收到消息的回调
             */
            channel.addConfirmListener(ackCallback, nackCallback);

            long begin = System.currentTimeMillis();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = "消息" + i;
                /*
                 * channel.getNextPublishSeqNo()获取下一个消息的序列号
                 * 通过序列号与消息体进行一个关联，Map中全部都是未确认的消息体
                 */
                outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            }
            long end = System.currentTimeMillis();
            log.info("{} 个消息异步发布确认，耗时：{} ms", MESSAGE_COUNT, (end - begin));
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
