package com.aishwarya.ethical.transparency_portal.modules.auth.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.aishwarya.ethical.transparency_portal.exception_handling.BadRequestException;
import com.aishwarya.ethical.transparency_portal.exception_handling.ConflictException;
import com.aishwarya.ethical.transparency_portal.exception_handling.UnAuthorizedException;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginRequest;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginResponse;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.RegisterRequest;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.RegisterResponse;
import com.aishwarya.ethical.transparency_portal.modules.user.model.CustomUserDetails;
import com.aishwarya.ethical.transparency_portal.modules.user.model.LoginResult;
import com.aishwarya.ethical.transparency_portal.modules.user.model.UserModel;
import com.aishwarya.ethical.transparency_portal.modules.user.service.UserService;
import com.aishwarya.ethical.transparency_portal.security.JWTUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {	

	@Value("${jwt.expiration}")
	private long tokenExpirationTime;
	
	private final JWTUtil jwtUtil;
	private final AuthenticationManager authenticationManager;
	private final UserService userService;

    public AuthenticationService(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
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
			System.out.println("Aish roles from service: "+roles);

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

    /**
     * Register a new user with comprehensive validation.
     * 
     * Security best practices implemented:
     * - Validates password match with confirmation
     * - Uses UserService for database operations with password encoding
     * - Proper exception handling and logging
     * - Returns user info without sensitive data
     * 
     * @param registerRequest Registration request containing username, email, and password
     * @return RegisterResponse with created user details
     * @throws BadRequestException if passwords don't match
     * @throws ConflictException if username or email already exists
     */
    public RegisterResponse register(RegisterRequest registerRequest) {
        log.info("Registration request received for email: {}", registerRequest.getEmail());

        // Validate password match
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            log.warn("Registration failed: Passwords do not match for email: {}", registerRequest.getEmail());
            throw new BadRequestException("Passwords do not match");
        }

        try {
            // Delegate to UserService for registration with password encoding
            UserModel registeredUser = userService.registerUser(
                    registerRequest.getDisplayName(),
                    registerRequest.getUsername(),
                    registerRequest.getEmail(),
                    registerRequest.getPassword()
            );

            log.info("User registration successful for: {}", registerRequest.getEmail());
            
            // Return registration response without sensitive data (no password or token)
            return RegisterResponse.of(
                    registeredUser.getId(),
                    registeredUser.getDisplayName(),
                    registeredUser.getUsername(),
                    registeredUser.getEmail(),
                    registeredUser.getRole()
            );

        } catch (Exception ex) {
            log.error("Registration failed for email {}: {}", registerRequest.getEmail(), ex.getMessage());
            throw ex;
        }
    }
}