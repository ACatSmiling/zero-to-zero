package cn.xisun.rabbitmq.controller;

import cn.xisun.rabbitmq.callback.MyConfirmCallBack;
import cn.xisun.rabbitmq.callback.MyNewConfirmCallBack;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author XiSun
 * @since 2023/10/18 10:49
 * <p>
 * 发布确认生产者
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ConfirmController {

    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private MyConfirmCallBack myConfirmCallBack;

    public ConfirmController() {
    }

    /**
     * 依赖注入rabbitTemplate之后，再设置它的回调对象
     */
    /*@PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(myConfirmCallBack);
    }*/

    @GetMapping("sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        log.info("发送消息：{}", message);

        // 指定消息id为1
        CorrelationData correlationData1 = new CorrelationData("1");
        String routingKey = "key1";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey, message + "_" + routingKey, correlationData1);

        // 指定消息id为2
        CorrelationData correlationData2 = new CorrelationData("2");
        routingKey = "key2";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey, message + "_" + routingKey, correlationData2);
    }

    @GetMapping("sendMessageNew/{message}")
    public void sendMessageNew(@PathVariable String message) {
        // 设置消息绑定一个id值，此处为UUID
        CorrelationData correlationData1 = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("confirm.exchange", "key1", message + "_" + "key1", correlationData1);
        log.info("发送消息id为：{}，消息内容：{}", correlationData1.getId(), message + "_" + "key1");

        CorrelationData correlationData2 = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("confirm.exchange", "key2", message + "_" + "key2", correlationData2);
        log.info("发送消息id为：{}，消息内容：{}", correlationData2.getId(), message + "_" + "key2");
    }
}
