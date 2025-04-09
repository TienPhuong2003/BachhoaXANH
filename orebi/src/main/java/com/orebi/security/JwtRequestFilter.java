package com.orebi.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.orebi.service.auth.impl.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(UserDetailsServiceImpl userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Lấy token từ header Authorization
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtTokenUtil.extractUsername(jwt);
        }
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Xác thực token
            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                Long userId = jwtTokenUtil.extractClaim(jwt, claims -> claims.get("userId", Long.class));
                String role = jwtTokenUtil.extractClaim(jwt, claims -> claims.get("role", String.class));
            
                // Tạo một CustomUserDetails với userId
                CustomUserDetails customUserDetails = new CustomUserDetails(
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities(),
                        userId,
                        role
                );
            
                // Tạo authentication token với CustomUserDetails
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        customUserDetails, null, customUserDetails.getAuthorities());
            
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }            
        }
        // Tiến hành filter tiếp theo trong chuỗi
        chain.doFilter(request, response);
    }
}
