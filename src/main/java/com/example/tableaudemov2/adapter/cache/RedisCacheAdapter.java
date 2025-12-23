package com.example.tableaudemov2.adapter.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheAdapter {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisCacheAdapter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /* ========= JTI Blacklist ========= */

    public void putBlacklistJti(String jti, long ttlMillis) {
        String key = RedisKeyGenerator.blacklistJti(jti);
        redisTemplate.opsForValue().set(key, "1", ttlMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isJtiBlacklisted(String jti) {
        String key = RedisKeyGenerator.blacklistJti(jti);
        Boolean exists = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(exists);
    }
}

