package com.example.tableaudemov2.service;

import com.example.tableaudemov2.adapter.cache.RedisCacheAdapter;
import com.example.tableaudemov2.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class AuthService {

    private final RedisCacheAdapter redisCacheAdapter;
    private final JwtUtil jwtUtil;   // ⭐ 注入 JwtUtil

    public AuthService(RedisCacheAdapter redisCacheAdapter,
                       JwtUtil jwtUtil) {
        this.redisCacheAdapter = redisCacheAdapter;
        this.jwtUtil = jwtUtil;
    }

    public void logout(String jwt) {
        log.info(">>> AuthService.logout called");

        // ✅ 使用注入的 JwtUtil（同一把 key）
        String jti = jwtUtil.getJti(jwt);
        Date expiration = jwtUtil.getExpiration(jwt);

        long ttlMillis = expiration.getTime() - System.currentTimeMillis();
        log.info("Put blacklist jti={}, ttl={}", jti, ttlMillis);

        if (ttlMillis > 0) {
            redisCacheAdapter.putBlacklistJti(jti, ttlMillis);
        }
    }
}
