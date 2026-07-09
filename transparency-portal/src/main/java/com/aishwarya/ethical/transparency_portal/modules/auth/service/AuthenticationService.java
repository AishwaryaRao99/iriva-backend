package com.aishwarya.ethical.transparency_portal.modules.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.aishwarya.ethical.transparency_portal.exception_handling.UnAuthorizedException;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginRequest;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginResponse;
import com.aishwarya.ethical.transparency_portal.modules.test.JWTUtil;
import com.aishwarya.ethical.transparency_portal.modules.user.model.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {

	@Autowired
	private JWTUtil jwtUtil;

	@Value("${jwt.expiration}")
	private long tokenExpirationTime;

	private AuthenticationManager authenticationManager;

	public LoginResponse authenticate(LoginRequest loginRequest) {

		try {
			// Step 1: Authenticate user
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			CustomUserDetails principal =
			        (CustomUserDetails) authentication.getPrincipal();
			
			Long id = principal.getId();

			// Step 2: Generate JWT token
			String jwtToken = jwtUtil.generateToken(loginRequest.getUsername(), java.util.List.of("ROLE_USER"));

			long expiresAt = System.currentTimeMillis() + tokenExpirationTime;

			log.info("JWT token generated successfully for user: {}", loginRequest.getUsername());

			// Step 3: Return response with token and user information
			return LoginResponse.of(jwtToken, loginRequest.getUsername(), id, expiresAt);

		} catch (UnAuthorizedException ex) {
			log.warn("Authentication failed: {}", ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			log.error("Unexpected error during authentication: {}", ex.getMessage());
			throw new UnAuthorizedException("Authentication failed: " + ex.getMessage());
		}
	}
}
