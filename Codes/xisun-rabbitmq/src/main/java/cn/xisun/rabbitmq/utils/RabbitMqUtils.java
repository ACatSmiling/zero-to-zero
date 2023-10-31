package cn.xisun.rabbitmq.utils;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @since 2023/10/13 10:36
 * <p>
 * Channel工具类
 */
public class RabbitMqUtils {

    /**
     * 获取一个连接的channel
     *
     * @return
     * @throws Exception
     */
    public static Channel getChannel() throws IOException, TimeoutException {
        // 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.2.100");
        factory.setUsername("rbmq");
        factory.setPassword("rbmq");
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }
}
