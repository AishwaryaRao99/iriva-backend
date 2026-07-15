package com.aishwarya.ethical.transparency_portal.modules.user.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aishwarya.ethical.transparency_portal.exception_handling.UserNotFoundException;
import com.aishwarya.ethical.transparency_portal.modules.user.model.UserModel;
import com.aishwarya.ethical.transparency_portal.modules.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
    	this.userRepository = userRepository;
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
}
