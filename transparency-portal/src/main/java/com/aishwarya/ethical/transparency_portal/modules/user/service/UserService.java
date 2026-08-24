package com.aishwarya.ethical.transparency_portal.modules.user.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aishwarya.ethical.transparency_portal.exception_handling.ConflictException;
import com.aishwarya.ethical.transparency_portal.exception_handling.ErrorCode;
import com.aishwarya.ethical.transparency_portal.exception_handling.UserNotFoundException;
import com.aishwarya.ethical.transparency_portal.modules.user.model.UserModel;
import com.aishwarya.ethical.transparency_portal.modules.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    	this.userRepository = userRepository;
    	this.passwordEncoder = passwordEncoder;
    }

    public UserModel findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User not found with username: {}", username);
                    return new UserNotFoundException("User not found with username: " + username);
                });
    }

    
    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new UserNotFoundException("User not found with email: " + email);
                });
    }
  
    public String getUser(Long id) {
        if (id == 0) {
            log.warn("Invalid user ID: {}", id);
            throw new UserNotFoundException("User not found with id: " + id);
        }
        return "User found with id: " + id;
    }

    
    public UserModel getUserDetailsByUsernameOrEmail(String usernameOrEmail) {
        Optional<UserModel> user = userRepository.findByUsername(usernameOrEmail);
        
        if (user.isPresent()) {
            return user.get();
        }

        return userRepository.findByEmail(usernameOrEmail)
                .orElseThrow(() -> {
                    log.warn("User not found with username or email: {}", usernameOrEmail);
                    return new UserNotFoundException("User not found with username or email: " + usernameOrEmail);
                });
    }

    /**
     * Register a new user if they don't already exist.
     * Applies best security practices:
     * - Checks for duplicate username and email
     * - Encodes password using BCrypt
     * - Sets default role as "USER"
     * - Timestamps account creation
     * 
     * @param username User's username
     * @param email User's email address
     * @param rawPassword User's plain text password
     * @return Created UserModel
     * @throws ConflictException if username or email already exists
     */
    public UserModel registerUser(String displayName, String username, String email, String rawPassword) {
        log.info("Attempting to register new user with username: {} and email: {}", username, email);

        // Check for existing username (case sensitivity handled at DB level)
        if (userRepository.existsByUsername(username)) {
            log.warn("Registration failed: Username already exists: {}", username);
            throw new ConflictException(ErrorCode.CONFLICT, "Username '" + username + "' is already taken");
        }

        // Check for existing email
        if (userRepository.existsByEmail(email)) {
            log.warn("Registration failed: Email already exists: {}", email);
            throw new ConflictException(ErrorCode.CONFLICT, "Email '" + email + "' is already registered");
        }

        try {
            // Create new user entity
            UserModel newUser = new UserModel();
            newUser.setUsername(username);
            newUser.setDisplayName(displayName.trim());
            newUser.setEmail(email);
            
            // Encode password using BCrypt (Spring Security best practice)
            String encodedPassword = passwordEncoder.encode(rawPassword);
            newUser.setPassword(encodedPassword);
            
            // Set default role to USER
            newUser.setRole("ROLE_USER");
            
            // Set creation timestamp
            LocalDateTime now = LocalDateTime.now();
            newUser.setCreatedAt(now);
            newUser.setUpdatedAt(now);

            // Save user to database
            UserModel savedUser = userRepository.save(newUser);
            log.info("User registered successfully: {} (ID: {})", username, savedUser.getId());

            return savedUser;

        } catch (ConflictException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error while registering user {}: {}", username, ex.getMessage(), ex);
            throw new RuntimeException("Failed to register user: " + ex.getMessage(), ex);
        }
    }
}