package com.nastyastrel.springbootrest.cache;

import com.nastyastrel.springbootrest.model.todo.TodoItemListWithNorrisJoke;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;


@Configuration
@EnableCaching
public class RedisConfig {
    public static final String TODO_ITEMS = "todoItems";

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheConfiguration redisCacheConfiguration = config
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(TodoItemListWithNorrisJoke.class)));
        return RedisCacheManager.builder(factory)
                .withCacheConfiguration(TODO_ITEMS, redisCacheConfiguration)
                .build();
    }

    @PostConstruct
    public void clearCache() {
        Jedis jedis = new Jedis(redisHost, redisPort, 1000);
        jedis.flushAll();
        jedis.close();
    }
}
