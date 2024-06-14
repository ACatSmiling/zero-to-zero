package cn.zero.cloud.platform.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/14 23:22
 */
@Slf4j
@Service
public class OrderService {
    public static final String ORDER_KEY = "order:";

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public OrderService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addOrder() {
        int keyId = ThreadLocalRandom.current().nextInt(1000) + 1;
        String orderNo = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(ORDER_KEY + keyId, "京东订单" + orderNo);
        log.info("编号{}的订单流水生成：{}", keyId, orderNo);
    }

    public String getOrderById(Integer id) {
        return (String) redisTemplate.opsForValue().get(ORDER_KEY + id);
    }
}
