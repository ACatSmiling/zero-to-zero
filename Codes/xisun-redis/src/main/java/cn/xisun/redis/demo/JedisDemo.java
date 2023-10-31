package cn.xisun.redis.demo;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @author XiSun
 * @since 2023/10/25 20:04
 */
@Slf4j
public class JedisDemo {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.2.100", 6379);

        jedis.auth("123456");

        log.info("redis connection status: {}", "连接成功");
        log.info("redis ping value: {}", jedis.ping());

        jedis.set("k1", "jedis");
        log.info("k1 value: {}", jedis.get("k1"));
    }
}
