package com.aishwarya.ethical.transparency_portal.modules.auth.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.aishwarya.ethical.transparency_portal.exception_handling.UnAuthorizedException;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginRequest;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginResponse;
import com.aishwarya.ethical.transparency_portal.modules.user.model.CustomUserDetails;
import com.aishwarya.ethical.transparency_portal.modules.user.model.LoginResult;
import com.aishwarya.ethical.transparency_portal.security.JWTUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {	

	@Value("${jwt.expiration}")
	private long tokenExpirationTime;
	
	private final JWTUtil jwtUtil;
	
	private final AuthenticationManager authenticationManager;

    public AuthenticationService(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    
	public LoginResult authenticate(LoginRequest loginRequest) {

		try {
			// Step 1: Authenticate user
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			CustomUserDetails principal =
			        (CustomUserDetails) authentication.getPrincipal();
			
			Long id = principal.getId();
			
			Collection<? extends GrantedAuthority> authorities =
					principal.getAuthorities();
			
			List<String> roles =
				    authorities.stream()
				               .map(GrantedAuthority::getAuthority)
				               .toList();
			

			// Step 2: Generate JWT token and store it in cookie
			String jwtToken = jwtUtil.generateToken(loginRequest.getUsername(), roles);

			long expiresAt = System.currentTimeMillis() + tokenExpirationTime;

			// Step 3: Return login result with JWT token and user info
			return LoginResult.of(jwtToken,LoginResponse.of(loginRequest.getUsername(), id, expiresAt));

		} catch (UnAuthorizedException ex) {
			log.warn("Authentication failed: {}", ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			log.error("Unexpected error during authentication: {}", ex.getMessage());
			throw new UnAuthorizedException("Authentication failed: " + ex.getMessage());
		}
	}
}
