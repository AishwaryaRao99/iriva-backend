package com.aishwarya.ethical.transparency_portal.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aishwarya.ethical.transparency_portal.modules.test.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String token = authHeader.substring(7);
        
        String username = jwtUtil.getUsernameFromToken(token);
        if (username!=null && jwtUtil.validateToken(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            List<String> roles = jwtUtil.getRolesFromToken(token);

            List<GrantedAuthority> authorities = roles.stream()
                    .map(r -> new SimpleGrantedAuthority(r.startsWith("ROLE_") ? r : "ROLE_" + r))
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            
            authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request));
            
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
