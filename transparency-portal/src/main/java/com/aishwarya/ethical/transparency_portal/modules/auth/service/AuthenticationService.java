package com.aishwarya.ethical.transparency_portal.modules.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aishwarya.ethical.transparency_portal.exception_handling.UnAuthorizedException;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginRequest;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginResponse;
import com.aishwarya.ethical.transparency_portal.modules.test.JWTUtil;
import com.aishwarya.ethical.transparency_portal.modules.user.model.UserModel;
import com.aishwarya.ethical.transparency_portal.modules.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Value("${jwt.expiration}")
    private long tokenExpirationTime;

    public LoginResponse authenticate(LoginRequest loginRequest) {
        String usernameOrEmail = loginRequest.getUsername();
        String providedPassword = loginRequest.getPassword();

        try {
            // Step 1: Find user by username or email
            UserModel user = userService.findByUsernameOrEmail(usernameOrEmail);
            log.info("User found for login attempt: {}", user.getUsername());

            // Step 2: Verify password
            if (!userService.verifyPassword(providedPassword, user.getPassword())) {
                log.warn("Failed login attempt for user: {} - Invalid password", user.getUsername());
                throw new UnAuthorizedException("Invalid credentials provided");
            }

            // Step 3: Generate JWT token
            // Note: Currently generating token with ROLE_USER, can be extended to fetch from database
            String jwtToken = jwtUtil.generateToken(user.getUsername(), java.util.List.of("ROLE_USER"));
            
            long expiresAt = System.currentTimeMillis() + tokenExpirationTime;
            
            log.info("JWT token generated successfully for user: {}", user.getUsername());

            // Step 4: Return response with token and user information
            return LoginResponse.of(
                    jwtToken,
                    user.getUsername(),
                    user.getId(),
                    expiresAt
            );

        } catch (UnAuthorizedException ex) {
            log.warn("Authentication failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error during authentication: {}", ex.getMessage());
            throw new UnAuthorizedException("Authentication failed: " + ex.getMessage());
        }
    }
}
