package com.orebi.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private final Long userId;
    private final String role;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Long userId, String role) {
        super(username, password, authorities);
        this.userId = userId;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }
    public String getRole(){
        return role;
    }
}
