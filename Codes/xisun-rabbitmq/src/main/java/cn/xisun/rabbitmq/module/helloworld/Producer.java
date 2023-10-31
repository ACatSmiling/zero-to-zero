package cn.xisun.rabbitmq.module.helloworld;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/12 22:33
 */
@Slf4j
public class Producer {

    // 队列名称
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        // 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.2.100");
        factory.setUsername("rbmq");
        factory.setPassword("rbmq");

        // channel实现了自动close接口，自动关闭，不需要显式关闭
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            /*
             * 生成一个队列：
             * 参数1：队列名称
             * 参数2：队列里面的消息是否持久化，默认消息存储在内存中
             * 参数3：该队列是否只供一个消费者进行消费，消息是否进行共享，true表示可以多个消费者消费，false表示只能一个消费者消费
             * 参数4：是否自动删除，最后一个消费者端开连接以后，该队列是否自动删除，true表示自动删除，false表示不自动删除
             * 参数5：其他参数，本示例暂不添加
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            String message = "hello world";
            /*
             * 发送一个消息：
             * 参数1：发送到哪个交换机，本示例使用的是默认的交换机
             * 参数2：路由的key是哪个，本示例使用的是队列的名称
             * 参数3：其他的参数信息，本示例暂不添加
             * 参数4：发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            log.info("消息发送完毕");
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
