package com.aishwarya.ethical.transparency_portal.modules.auth.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.aishwarya.ethical.transparency_portal.exception_handling.UnAuthorizedException;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginRequest;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginResponse;
import com.aishwarya.ethical.transparency_portal.modules.user.model.CustomUserDetails;
import com.aishwarya.ethical.transparency_portal.security.JWTUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {
	
	

	@Value("${jwt.expiration}")
	private long tokenExpirationTime;
	
	private final AuthenticationManager authenticationManager;

    public AuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

	public LoginResponse authenticate(LoginRequest loginRequest) {

		try {
			// Step 1: Authenticate user
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			CustomUserDetails principal =
			        (CustomUserDetails) authentication.getPrincipal();
			
			Long id = principal.getId();

			long expiresAt = System.currentTimeMillis() + tokenExpirationTime;

			// Step 3: Return response user information
			return LoginResponse.of(loginRequest.getUsername(), id, expiresAt);

		} catch (UnAuthorizedException ex) {
			log.warn("Authentication failed: {}", ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			log.error("Unexpected error during authentication: {}", ex.getMessage());
			throw new UnAuthorizedException("Authentication failed: " + ex.getMessage());
		}
	}
}
