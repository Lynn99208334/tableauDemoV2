package com.example.tableaudemov2.service;

import com.example.tableaudemov2.entity.User;
import com.example.tableaudemov2.repository.UserRepository;
import com.example.tableaudemov2.security.JpaUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
class UserRegisterTest {

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("Test");
        user.setEmail("test@example.com");
        user.setPassword("123456");
        user.setEmailVerified(false); // ❗故意不驗證
        userRepository.save(user);
    }

    @Test
    void test_login_success_even_if_email_not_verified_in_test_profile() {

        // when / then
        assertDoesNotThrow(() -> {
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername("test@example.com");

            assertThat(userDetails).isNotNull();
            assertThat(userDetails.getUsername()).isEqualTo("test@example.com");
        });
    }
}
