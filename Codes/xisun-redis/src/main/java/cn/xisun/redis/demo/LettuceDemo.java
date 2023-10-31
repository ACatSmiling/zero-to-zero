package cn.xisun.redis.demo;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SortArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author XiSun
 * @since 2023/10/25 20:16
 */
@Slf4j
public class LettuceDemo {
    public static void main(String[] args) {
        // 使用构建器 RedisURI.Builder
        RedisURI uri = RedisURI.Builder
                .redis("192.168.2.100")
                .withPort(6379)
                .withAuthentication("default", "123456")
                .build();

        // 创建连接客户端
        RedisClient client = RedisClient.create(uri);

        StatefulRedisConnection conn = client.connect();

        // 操作命令api
        RedisCommands<String, String> commands = conn.sync();

        // keys
        List<String> list = commands.keys("*");
        for (String s : list) {
            log.info("key: {}", s);
        }

        // String
        commands.set("k1", "1111");
        String s1 = commands.get("k1");
        log.info("String, s1: {}", s1);

        // list
        commands.lpush("myList2", "v1", "v2", "v3");
        List<String> list2 = commands.lrange("myList2", 0, -1);
        for (String s : list2) {
            log.info("list s: {}", s);
        }

        // set
        commands.sadd("mySet2", "v1", "v2", "v3");
        Set<String> set = commands.smembers("mySet2");
        for (String s : set) {
            log.info("set s: {}", s);
        }

        // hash
        Map<String, String> map = new HashMap<>();
        map.put("k1", "138xxxxxxxx");
        map.put("k2", "atguigu");
        map.put("k3", "zzyybs@126.com");

        commands.hmset("myHash2", map);
        Map<String, String> retMap = commands.hgetall("myHash2");
        for (String k : retMap.keySet()) {
            log.info("hash, key: {}, value: {}", k, retMap.get(k));
        }

        // zset
        commands.zadd("myZset2", 100.0, "s1", 110.0, "s2", 90.0, "s3");
        List<String> list3 = commands.zrange("myZset2", 0, 10);
        for (String s : list3) {
            log.info("zset, s: {}", s);
        }

        // sort
        SortArgs sortArgs = new SortArgs();
        sortArgs.alpha();
        sortArgs.desc();

        List<String> list4 = commands.sort("myList2", sortArgs);
        for (String s : list4) {
            log.info("sort, s: {}", s);
        }

        // 关闭
        conn.close();
        client.shutdown();
    }
}
