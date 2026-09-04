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

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	
	private final JWTUtil jwtUtil;
	
	public JwtAuthenticationFilter (JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {

			for (Cookie cookie : cookies) {
				if ("jwt".equals(cookie.getName())) {
					String token = cookie.getValue();

					String username = jwtUtil.getUsernameFromToken(token);
					if (username != null && jwtUtil.validateToken(token)
							&& SecurityContextHolder.getContext().getAuthentication() == null) {

						List<String> roles = jwtUtil.getRolesFromToken(token);

						List<GrantedAuthority> authorities = roles.stream()
								.map(r -> new SimpleGrantedAuthority(r.startsWith("ROLE_") ? r : "ROLE_" + r))
								.collect(Collectors.toList());

						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
								username, null, authorities);

						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}
			}
		}

		filterChain.doFilter(request, response);
	}
}
