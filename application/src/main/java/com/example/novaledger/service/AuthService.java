package com.example.novaledger.service;

import com.example.novaledger.adapter.cache.RedisCacheAdapter;
import com.example.novaledger.auth.dto.AuthResponse;
import com.example.novaledger.auth.dto.LoginRequest;
import com.example.novaledger.auth.dto.RegisterRequest;
import com.example.novaledger.auth.entity.Role;
import com.example.novaledger.auth.entity.Tenant;
import com.example.novaledger.auth.entity.User;
import com.example.novaledger.auth.entity.UserTenant;
import com.example.novaledger.auth.enums.UserStatus;
import com.example.novaledger.auth.enums.UserTenantStatus;
import com.example.novaledger.auth.jwt.JwtTokenProvider;
import com.example.novaledger.auth.repository.RoleRepository;
import com.example.novaledger.auth.repository.TenantRepository;
import com.example.novaledger.auth.repository.UserRepository;
import com.example.novaledger.auth.repository.UserTenantRepository;
import com.example.novaledger.common.exception.BusinessException;
import com.example.novaledger.common.exception.ErrorCode;
import com.example.novaledger.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class AuthService {

    private final RedisCacheAdapter redisCacheAdapter;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TenantRepository tenantRepository;
    private final UserTenantRepository userTenantRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${app.auth.resend-cooldown-seconds:60}")
    private long resendCooldownSeconds;

    public AuthService(RedisCacheAdapter redisCacheAdapter,
                       JwtUtil jwtUtil,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder, EmailService emailService, TenantRepository tenantRepository, UserTenantRepository userTenantRepository, RoleRepository roleRepository, JwtTokenProvider jwtTokenProvider) {
        this.redisCacheAdapter = redisCacheAdapter;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tenantRepository = tenantRepository;
        this.userTenantRepository = userTenantRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
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
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        String verifyToken = generateEmailVerifyToken();
        LocalDateTime expiredAt = generateEmailVerifyExpiredAt();

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setStatus(UserStatus.REGISTERED);
        user.setEmailVerified(false);
        user.setEnabled(true);

        // ⭐ 新增的三行
        user.setEmailVerifyToken(verifyToken);
        user.setEmailVerifyExpiredAt(expiredAt);

        userRepository.save(user);

        // 建立預設個人 Tenant
        Tenant tenant = new Tenant();
        tenant.setCode(UUID.randomUUID().toString().substring(0, 8));
        tenant.setName(request.getUsername() + "'s Ledger");
        tenant.setType("PERSONAL");
        tenant.setPlan("FREE");
        tenant.setOwnerUserId(user.getId());
        tenant.setStatus("ACTIVE");
        tenantRepository.save(tenant);

        // 查 OWNER role
        Role ownerRole = roleRepository.findByCodeAndTenantIdIsNull("OWNER")
                .orElseThrow(() -> new IllegalStateException("OWNER role not found"));

        // 建立 UserTenant 關聯
        UserTenant userTenant = new UserTenant();
        userTenant.setUserId(user.getId());
        userTenant.setTenantId(tenant.getId());
        userTenant.setRoleId(ownerRole.getId());
        userTenant.setStatus(UserTenantStatus.ACTIVE);
        userTenant.setJoinedAt(LocalDateTime.now());
        userTenantRepository.save(userTenant);

        // ⭐ 寄送驗證信
        String verifyLink =
                "http://localhost:8111/api/auth/verify-email?token=" + verifyToken;

        emailService.sendVerifyEmail(user.getEmail(), verifyLink);
    }

    private String generateEmailVerifyToken() {
        return UUID.randomUUID().toString();
    }

    private LocalDateTime generateEmailVerifyExpiredAt() {
        return LocalDateTime.now().plusHours(24);
    }

    // ========================
// 登入
// ========================
    public AuthResponse login(LoginRequest request) {

        // 1. 查帳號
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));

        // 2. 驗證密碼
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        // 3. 查 UserTenant（取第一個 ACTIVE 的）
        UserTenant userTenant = userTenantRepository
                .findFirstByUserIdAndStatus(user.getId(), UserTenantStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));

        // 4. 查 Role
        Role role = roleRepository.findById(userTenant.getRoleId())
                .orElseThrow(() -> new IllegalStateException("Role not found"));

        // 5. 產生 token
        List<String> roles = List.of(role.getCode());
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(), userTenant.getTenantId(), roles);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return new AuthResponse(accessToken, refreshToken);
    }

}
