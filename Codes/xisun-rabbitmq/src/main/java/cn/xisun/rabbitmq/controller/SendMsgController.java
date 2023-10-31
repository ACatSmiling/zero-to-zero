package cn.xisun.rabbitmq.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author XiSun
 * @since 2023/10/16 22:44
 * <p>
 * 延迟队列，消息生产者，因为消息没有定义消息的消费者，因此，消息达到TTL时间后，会发送到死信队列
 */
@Slf4j
@RequestMapping("ttl")
@RestController
public class SendMsgController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间：{}，发送一条信息给两个TTL队列：{}", new Date(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自TTL为10秒的队列：" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自TTL为40秒的队列：" + message);
    }

    @GetMapping("sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message, @PathVariable String ttlTime) {
        log.info("当前时间：{}，发送一条TTL为{}毫秒的信息给队列C：{}", new Date(), ttlTime, message);
        rabbitTemplate.convertAndSend("X", "XC", message, correlationData -> {
            // 设置消息的TTL
            correlationData.getMessageProperties().setExpiration(ttlTime);
            return correlationData;
        });
    }

    private static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";

    private static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    @GetMapping("sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime) {
        log.info(" 当前时间：{}，发送一条TTL为{}毫秒的信息给队列delayed.queue：{}", new Date(), delayTime, message);
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_NAME, DELAYED_ROUTING_KEY, message, correlationData -> {
            correlationData.getMessageProperties().setDelay(delayTime);
            return correlationData;
        });
    }
}
