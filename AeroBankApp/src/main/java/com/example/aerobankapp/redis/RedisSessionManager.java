package com.example.aerobankapp.redis;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

public class RedisSessionManager
{
    private RedisProperties.Jedis jedis;

    public RedisSessionManager(String redisHost, int redisPort)
    {
     
    }
}
