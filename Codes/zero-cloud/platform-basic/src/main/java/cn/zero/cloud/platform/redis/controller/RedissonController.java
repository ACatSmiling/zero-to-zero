package cn.zero.cloud.platform.redis.controller;

import cn.zero.cloud.platform.redis.service.RedissonApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/20 22:45
 */
@RestController
@RequestMapping(value = "/redis")
public class RedissonController {
    private final RedissonApiService redissonApiService;

    @Autowired
    public RedissonController(RedissonApiService redissonApiService) {
        this.redissonApiService = redissonApiService;
    }

    @GetMapping(value = "/bloom/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void addElementToBloomFilter(@RequestParam String key) {
        redissonApiService.addElementToBloomFilter(key);
    }

    @GetMapping(value = "/bloom/check", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void checkElementWithBloomFilter(@RequestParam String key) {
        redissonApiService.checkElementWithBloomFilter(key);
    }
}
