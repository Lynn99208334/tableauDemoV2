package com.example.novaledger.security;

import com.example.novaledger.auth.entity.User;
import com.example.novaledger.auth.repository.UserRepository;
import com.example.novaledger.common.exception.BusinessException;
import com.example.novaledger.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Value("${app.auth.skip-email-verify:false}")
    private boolean skipEmailVerify;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(">>> loadUserByUsername called with: " + email);
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage())
                    );
            System.out.println(">>> user found: " + user.getEmail());
            System.out.println(">>> emailVerified: " + user.getEmailVerified());

            if (!skipEmailVerify && !Boolean.TRUE.equals(user.getEmailVerified())) {
                throw new BusinessException(ErrorCode.EMAIL_NOT_VERIFIED);
            }
            System.out.println(">>> raw password from DB: [" + user.getPassword() + "]");
            System.out.println(">>> manual check 123456789: " + new BCryptPasswordEncoder().matches("123456789", user.getPassword()));
            return new SecurityUser(user);
        } catch (Exception e) {
            System.out.println(">>> EXCEPTION in loadUserByUsername: " + e.getClass().getName() + " - " + e.getMessage());
            throw e;
        }
    }
}
