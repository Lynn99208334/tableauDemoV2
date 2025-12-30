package com.example.tableaudemov2.service;

import com.example.tableaudemov2.entity.User;
import com.example.tableaudemov2.enums.ErrorCode;
import com.example.tableaudemov2.exception.BusinessException;
import com.example.tableaudemov2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EmailVerificationServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Test
    //確保過期 token 一定會被擋
    /**
     * Test #1
     * token 過期 → 驗證失敗
     */
    void verifyEmail_shouldFail_whenTokenExpired() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@testgmail.com");
        user.setPassword("password");
        user.setEmailVerifyToken("expired-token");
        user.setEmailVerifyExpiredAt(LocalDateTime.now().minusMinutes(1));
        user.setEmailVerified(false);

        userRepository.save(user);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> authService.verifyEmail("expired-token")
        );

        assertEquals(ErrorCode.EMAIL_VERIFY_TOKEN_EXPIRED, ex.getErrorCode());
    }

    /**
     * Test #2
     * resend 在冷卻時間內 → 應該被擋
     */
    @Test
    void resendVerification_shouldFail_whenInCooldown() {

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("cooldown@gmail.com");
        user.setEmailVerified(false);

        // 模擬「剛寄過信」
        user.setEmailVerifyToken("recent-token");
        user.setEmailVerifyExpiredAt(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> authService.resendVerificationEmail("cooldown@gmail.com")
        );

        assertEquals(ErrorCode.EMAIL_RESEND_TOO_FREQUENT, ex.getErrorCode());
    }

    /**
     * Test #3
     * resend 正常 → token 會被更新
     */
    @Test
    void resendVerification_shouldUpdateToken_whenAllowed() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@testgmail.com");
        user.setPassword("password");
        user.setEmailVerified(false);
        user.setEmailVerifyToken("old-token");
        user.setEmailVerifyExpiredAt(LocalDateTime.now().minusMinutes(15));

        userRepository.save(user);

        authService.resendVerificationEmail("test@testgmail.com");

        User updated = userRepository.findByEmail("test@testgmail.com")
                .orElseThrow();

        assertNotNull(updated.getEmailVerifyToken());
        assertNotEquals("old-token", updated.getEmailVerifyToken());
        assertTrue(updated.getEmailVerifyExpiredAt().isAfter(LocalDateTime.now()));
    }

}
