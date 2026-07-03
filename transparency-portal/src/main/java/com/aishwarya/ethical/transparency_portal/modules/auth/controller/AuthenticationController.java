package com.aishwarya.ethical.transparency_portal.modules.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginRequest;
import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginResponse;
import com.aishwarya.ethical.transparency_portal.modules.auth.service.AuthenticationService;

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
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getUsername());

        try {
            // Authenticate user and generate token
            LoginResponse loginResponse = authenticationService.authenticate(loginRequest);
            
            log.info("Login successful for user: {}", loginRequest.getUsername());

            return ResponseEntity.ok(loginResponse);

        } catch (Exception ex) {
            log.error("Login failed for user: {} - Error: {}", loginRequest.getUsername(), ex.getMessage());
            throw ex; // Let GlobalExceptionHandler handle it
        }
    }
}
