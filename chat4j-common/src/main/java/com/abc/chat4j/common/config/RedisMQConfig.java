package com.abc.chat4j.common.config;

import com.abc.chat4j.common.core.mq.redis.RedisMQTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisMQConfig {

    @Bean
    public RedisMQTemplate redisMQTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisMQTemplate redisMQTemplate = new RedisMQTemplate();
        redisMQTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置值（value）的序列化采用FastJsonRedisSerializer
        redisMQTemplate.setValueSerializer(jacksonRedisSerializer());
        redisMQTemplate.setHashValueSerializer(jacksonRedisSerializer());
        // 设置键（key）的序列化采用StringRedisSerializer。
        redisMQTemplate.setKeySerializer(new StringRedisSerializer());
        redisMQTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisMQTemplate.afterPropertiesSet();
        return redisMQTemplate;
    }
    @Bean
    public Jackson2JsonRedisSerializer<Object> jacksonRedisSerializer() {
        return new Jackson2JsonRedisSerializer<>(Object.class);
    }
}