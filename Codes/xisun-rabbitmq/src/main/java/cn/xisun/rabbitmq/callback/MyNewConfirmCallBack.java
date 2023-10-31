package cn.xisun.rabbitmq.callback;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

/**
 * @author XiSun
 * @since 2023/10/18 12:58
 * <p>
 * 发布确认回调函数，设置回退消息处理方式
 */
@Slf4j
@Component
public class MyNewConfirmCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 依赖注入rabbitTemplate之后，再设置它的回调对象
     */
    @PostConstruct
    private void init() {
        rabbitTemplate.setConfirmCallback(this);
        /*
         * mandatory参数值：
         *      true：交换机无法将消息进行路由时，会将该消息返回给生产者
         *      false：交换机无法将消息进行路由时，会将该消息直接丢弃
         */
        rabbitTemplate.setMandatory(true);
        // 设置回退消息交给谁处理
        rabbitTemplate.setReturnsCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机收到消息并确认成功，id：{}", id);
        } else {
            log.error("消息未成功投递到交换机，id：{}，原因：{}", id, cause);
        }
    }

    /**
     * 回退消息处理方法
     *
     * @param returned the returned message and metadata.
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        int replyCode = returned.getReplyCode();
        Message message = returned.getMessage();
        String replyText = returned.getReplyText();
        String exchange = returned.getExchange();
        String routingKey = returned.getRoutingKey();
        log.info("消息被回退，回退编码：{}，回退消息：{}，回退原因：{}，交换机：{}，routingKey：{}",
                replyCode, message.getBody(), replyText, exchange, routingKey);
    }
}
