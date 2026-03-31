package com.example.novaledger.security;

import com.example.novaledger.auth.entity.UserTenant;
import com.example.novaledger.auth.repository.UserTenantRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserTenantRepository userTenantRepository;

    public LoginSuccessHandler(UserTenantRepository userTenantRepository) {
        this.userTenantRepository = userTenantRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long userId = securityUser.getUserId();

        Long tenantId = userTenantRepository.findByUserId(userId)
                .stream()
                .findFirst()
                .map(UserTenant::getTenantId)
                .orElse(null);

        HttpSession session = request.getSession();
        System.out.println(">>> LoginSuccessHandler: userId=" + userId);
        System.out.println(">>> LoginSuccessHandler: tenantId=" + tenantId);
        session.setAttribute("tenantId", tenantId);
        session.setAttribute("userId", userId);

        response.sendRedirect("/page/dashboard");
    }
}