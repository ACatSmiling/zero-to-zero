package cn.xisun.rabbitmq.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author XiSun
 * @since 2023/10/18 10:51
 * <p>
 * 发布确认回调函数
 */
@Slf4j
@Component
public class MyConfirmCallBack implements RabbitTemplate.ConfirmCallback {

    /**
     * 交换机不管是否收到消息的一个回调方法
     *
     * @param correlationData correlation data for the callback.
     * @param ack             true for ack, false for nack
     * @param cause           An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机确认已经收到id为{}的消息", id);
        } else {
            log.info("交换机确认未收到id为{}消息，原因：{}", id, cause);
        }
    }
}
