package com.aishwarya.ethical.transparency_portal.modules.auth.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginRequest;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.RegisterRequest;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.RegisterResponse;
import com.aishwarya.ethical.transparency_portal.modules.auth.service.AuthenticationService;
import com.aishwarya.ethical.transparency_portal.modules.user.model.LoginResult;
import com.aishwarya.ethical.transparency_portal.security.JWTUtil;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	public AuthenticationController(JWTUtil jwtUtil, AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("Login attempt for user: {}", loginRequest.getUsername());

		try {
			// Authenticate user and store user info
			LoginResult loginResult = authenticationService.authenticate(loginRequest);

			ResponseCookie cookie = ResponseCookie.from("jwt", loginResult.getJwt()).httpOnly(true).secure(false)
					.path("/").maxAge(Duration.ofHours(1)).sameSite("Lax") // in prod it is none since we use different domains -
					.build();														// vercel and render for each
					
			log.info("Login successful for user: {}", loginRequest.getUsername());

			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
					.body(loginResult.getLoginResponse());

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

	/**
	 * Register a new user with email and password.
	 * 
	 * Implements security best practices:
	 * - Validates username, email, and password format
	 * - Ensures password confirmation matches
	 * - Checks for duplicate users in database
	 * - Encodes password using BCrypt
	 * 
	 * @param registerRequest Contains username, email, password, and confirmPassword
	 * @return RegisterResponse with user details (no sensitive data)
	 * @throws BadRequestException if validation fails (passwords don't match, invalid format)
	 * @throws ConflictException if username or email already exists in database
	 */
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
		log.info("Registration attempt for email: {}", registerRequest.getEmail());

		try {
			// Register user via AuthenticationService
			RegisterResponse registerResponse = authenticationService.register(registerRequest);

			log.info("Registration successful for user: {} (ID: {})", registerRequest.getUsername(), registerResponse.getUserId());

			// Return 201 Created status with registration details
			return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(registerResponse);

		} catch (Exception ex) {
			log.error("Registration failed for email: {} - Error: {}", registerRequest.getEmail(), ex.getMessage());
			throw ex; // Let GlobalExceptionHandler handle it
		}
	}
}
