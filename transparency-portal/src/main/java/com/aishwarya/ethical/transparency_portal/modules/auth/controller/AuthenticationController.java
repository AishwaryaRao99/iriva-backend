package com.aishwarya.ethical.transparency_portal.modules.auth.controller;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginRequest;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginResponse;
import com.aishwarya.ethical.transparency_portal.modules.auth.service.AuthenticationService;
import com.aishwarya.ethical.transparency_portal.security.JWTUtil;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;
	private final JWTUtil jwtUtil;

	public AuthenticationController(JWTUtil jwtUtil, AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("Login attempt for user: {}", loginRequest.getUsername());

		try {
			// Authenticate user and store user info
			LoginResponse loginResponse = authenticationService.authenticate(loginRequest);

			// Step 2: Generate JWT token and store it in cookie
			String jwtToken = jwtUtil.generateToken(loginRequest.getUsername(), java.util.List.of("ROLE_USER"));

			ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken).httpOnly(true).secure(false).path("/")
					.maxAge(Duration.ofHours(1)).sameSite("Lax") // in prod it is none since we use different domains -
																	// vercel and render for each
					.build();

			log.info("Login successful for user: {}", loginRequest.getUsername());

			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(loginResponse);

		} catch (Exception ex) {
			log.error("Login failed for user: {} - Error: {}", loginRequest.getUsername(), ex.getMessage());
			throw ex; // Let GlobalExceptionHandler handle it
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout() {

		ResponseCookie cookie = ResponseCookie.from("jwt", "").httpOnly(true).secure(false) // true in production
				.path("/").sameSite("Lax").maxAge(0).build();

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("Logout successful");
	}
}
