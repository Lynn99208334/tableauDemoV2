package com.example.tableaudemov2.service;

import com.example.tableaudemov2.adapter.cache.RedisCacheAdapter;
import com.example.tableaudemov2.dto.RegisterRequest;
import com.example.tableaudemov2.entity.User;
import com.example.tableaudemov2.enums.UserStatus;
import com.example.tableaudemov2.repository.UserRepository;
import com.example.tableaudemov2.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class AuthService {

    private final RedisCacheAdapter redisCacheAdapter;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(RedisCacheAdapter redisCacheAdapter,
                       JwtUtil jwtUtil,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.redisCacheAdapter = redisCacheAdapter;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ========================
    // 登出（JWT Blacklist）
    // ========================
    public void logout(String jwt) {
        log.info(">>> AuthService.logout called");

        String jti = jwtUtil.getJti(jwt);
        Date expiration = jwtUtil.getExpiration(jwt);

        long ttlMillis = expiration.getTime() - System.currentTimeMillis();
        log.info("Put blacklist jti={}, ttl={}", jti, ttlMillis);

        if (ttlMillis > 0) {
            redisCacheAdapter.putBlacklistJti(jti, ttlMillis);
        }
    }

    // ========================
    // 註冊
    // ========================
    @Transactional
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("EMAIL_ALREADY_EXISTS");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("USERNAME_ALREADY_EXISTS");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setStatus(UserStatus.REGISTERED);
        user.setEmailVerified(false);
        user.setEnabled(true);

        userRepository.save(user);
    }
}
