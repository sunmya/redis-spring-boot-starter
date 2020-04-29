package com.wuku.stater.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wuku.stater.redis.properties.RedisProperties;
import com.wuku.stater.redis.redisClient.JedisClient;
import com.wuku.stater.redis.redisClient.JedisClusterClient;
import com.wuku.stater.redis.service.RedisService;

/**
 * 
 * @author SunMy
 *
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfig {
    @Autowired
    private RedisProperties redisProperties;

    @Bean(name = "redisService")
    public RedisService redisService(){
    	if (redisProperties.iscluster()) {
			return new JedisClusterClient(redisProperties);
		} else {
			return new JedisClient(redisProperties);
		}
    }
}