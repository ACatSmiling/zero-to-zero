package cn.xisun.redis.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author XiSun
 * @since 2023/10/25 22:01
 */
@Slf4j
@Service
public class OrderService {

    public static final String ORDER_KEY = "order:";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void addOrder() {
        int keyId = ThreadLocalRandom.current().nextInt(1000) + 1;
        String orderNo = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(ORDER_KEY + keyId, "京东订单" + orderNo);
        log.info("编号{}的订单流水生成：{}", keyId, orderNo);
    }

    public String getOrderById(Integer id) {
        return (String) redisTemplate.opsForValue().get(ORDER_KEY + id);
    }

    /**
     * 延迟双删
     *
     * @param order
     */
    /*public void deleteOrderData(Order order) {
        // 1. 更新MySQL数据库之前，删除Redis缓存
        redisTemplate.delete(ORDER_KEY + order.getId());

        // 2. 更新MySQL数据库
        orderDao.update(order);

        // 延迟一段时间，等待其他业务可能出现的访问请求完成
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 3. 更新MySQL数据库之后，再删除Redis缓存
        redisTemplate.delete(ORDER_KEY + order.getId());
    }*/

    /**
     * 延迟双删，优化加大吞吐量
     *
     * @param order
     */
    /*public void deleteOrderData2(Order order) {
        // 1. 更新MySQL数据库之前，删除Redis缓存
        redisTemplate.delete(ORDER_KEY + order.getId());

        // 2. 更新MySQL数据库
        orderDao.update(order);

        // 延迟一段时间，等待其他业务可能出现的访问请求完成
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        CompletableFuture.supplyAsync(() -> {
            // 3. 更新MySQL数据库之后，再删除Redis缓存。使用异步删除，加大吞吐量
            return redisTemplate.delete(ORDER_KEY + order.getId());
        }).whenComplete((t, u) -> {
            System.out.println("t：" + t);
            System.out.println("u：" + u);
        }).exceptionally(e -> {
            System.out.println("e：" + e.getMessage());
            return 44L;
        }).get();
    }*/
}
