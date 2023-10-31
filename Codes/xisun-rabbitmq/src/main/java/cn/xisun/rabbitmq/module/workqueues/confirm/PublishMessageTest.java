package cn.xisun.rabbitmq.module.workqueues.confirm;

import lombok.extern.slf4j.Slf4j;

/**
 * @author XiSun
 * @since 2023/10/13 22:45
 */
@Slf4j
public class PublishMessageTest {
    public static void main(String[] args) {

        PublishMessageIndividually.publishMessageIndividually();

         PublishMessageBatch.publishMessageBatch();

         PublishMessageAsync.publishMessageAsync();
    }
}
