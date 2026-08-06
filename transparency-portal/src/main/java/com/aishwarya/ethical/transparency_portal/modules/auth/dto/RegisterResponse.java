package com.aishwarya.ethical.transparency_portal.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {

    private Long userId;
    private String username;
    private String email;
    private String role;
    private String message;

    public static RegisterResponse of(Long userId, String username, String email, String role) {
        return new RegisterResponse(userId, username, email, role, "User registered successfully");
    }
}
