package com.example.tableaudemov2.service;

import com.example.tableaudemov2.adapter.cache.RedisCacheAdapter;
import com.example.tableaudemov2.security.JwtTokenProvider;
import com.example.tableaudemov2.testutil.TestSecurityContextFactory;
import com.example.tableaudemov2.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private RedisCacheAdapter redisCacheAdapter;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void logout_should_blacklist_jti() {
        // 1️⃣ 用正式系統產 JWT（不是自己造）
        String jwt = jwtTokenProvider.generateToken(
                TestSecurityContextFactory.authenticatedAdmin()
        );

        // 2️⃣ 呼叫 logout
        authService.logout(jwt);

        // 3️⃣ 用同一個 JwtUtil 解析 jti
        String jti = jwtUtil.getJti(jwt);

        // 4️⃣ 驗證 Redis blacklist
        assertTrue(redisCacheAdapter.isJtiBlacklisted(jti));
    }
}
