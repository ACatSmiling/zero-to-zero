package cn.zero.cloud.platform.config;

import cn.zero.cloud.platform.config.properties.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Xisun Wang
 * @since 6/7/2024 14:50
 */
@Configuration
public class RedisConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);

    private final RedisProperties redisProperties;

    @Autowired
    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
        LOGGER.info("Using redis {} ", this.redisProperties.isEnableRedis());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 设置key的序列化方式为String
        template.setKeySerializer(new StringRedisSerializer());

        // 设置value的序列化方式为JSON，这样可以将Java对象直接存储为JSON字符串
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 设置hash key和hash value的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 初始化RedisTemplate序列化设置
        template.afterPropertiesSet();

        return template;
    }
}
