package com.aishwarya.ethical.transparency_portal.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String tokenType;

    private String username;

    private Long userId;

    private Long expiresIn;

    public static LoginResponse of(String username, Long userId, Long expiresIn) {
        return new LoginResponse( "Bearer", username, userId, expiresIn);
    }
}
