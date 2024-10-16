package com.trelloproject.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisSentinelConfig {

    @Value("${REDIS_SENTINEL_MASTER}")
    private String sentinelMaster;

    @Value("${REDIS_SENTINEL_NODES}")
    private String sentinelNodes;

    @Value("${REDIS_HOST_NAME}")
    private String standaloneHost;

    @Value("${REDIS_PORT}")
    private int standalonePort;

    // Sentinel Connection Factory for Card usage
    @Bean
    public RedisConnectionFactory sentinelConnectionFactory() {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(sentinelMaster)
                .sentinel(sentinelNodes.split(",")[0], 26379)
                .sentinel(sentinelNodes.split(",")[1], 26379);
        return new LettuceConnectionFactory(sentinelConfig);
    }

    // Standalone Connection Factory for general usage
    @Bean
    public RedisConnectionFactory standaloneConnectionFactory() {
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(standaloneHost, standalonePort);
        return new LettuceConnectionFactory(standaloneConfig);
    }

    // Sentinel RedisTemplate for Card functionality
    @Bean(name = "sentinelRedisTemplate")
    public RedisTemplate<String, Object> sentinelRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(sentinelConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    // General RedisTemplate for other functionalities
    @Bean(name = "standaloneRedisTemplate")
    public RedisTemplate<String, Object> standaloneRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(standaloneConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
