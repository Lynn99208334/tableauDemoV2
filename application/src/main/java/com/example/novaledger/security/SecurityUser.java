package com.example.novaledger.security;

import com.example.novaledger.auth.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security 用的 UserDetails Adapter
 * 目前階段：不處理角色與權限（RBAC 之後再加）
 */
public class SecurityUser implements UserDetails {

    private final User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    /**
     * 權限清單（目前為空）
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * 登入帳號（建議用 email）
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * 帳號是否未過期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 帳號是否未鎖定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 憑證是否未過期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 系統層是否啟用（只看 enabled）
     */
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getEnabled());
    }

    /**
     * （選用）取得原始 User
     */
    @Override
    public String getPassword() {
        System.out.println(">>> SecurityUser.getPassword() returning: [" + user.getPassword() + "]");
        return user.getPassword();
    }


}
