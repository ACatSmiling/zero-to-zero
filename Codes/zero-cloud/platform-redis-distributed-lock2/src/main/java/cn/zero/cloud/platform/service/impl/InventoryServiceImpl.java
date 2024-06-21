package cn.zero.cloud.platform.service.impl;

import cn.zero.cloud.platform.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/21 22:22
 */
@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {
    @Value("${server.port}")
    private String port;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public InventoryServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private final Lock lock = new ReentrantLock();

    @Override
    public String sale() {
        String retMessage;
        lock.lock();
        try {
            // 1 查询库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory001");
            // 2 判断库存是否足够
            int inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            // 3 扣减库存
            if (inventoryNumber > 0) {
                redisTemplate.opsForValue().set("inventory001", String.valueOf(--inventoryNumber));
                retMessage = "成功卖出一个商品，库存剩余: " + inventoryNumber;
                System.out.println(retMessage);
            } else {
                retMessage = "商品卖完了，o(╥﹏╥)o";
            }
        } finally {
            lock.unlock();
        }
        return retMessage + "\t" + "服务端口号：" + port;
    }
}
