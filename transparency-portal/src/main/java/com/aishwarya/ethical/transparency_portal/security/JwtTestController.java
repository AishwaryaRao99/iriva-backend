package com.aishwarya.ethical.transparency_portal.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class JwtTestController {

    private final JWTUtil jwtUtil;

    public JwtTestController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/token")
    public String token() {

        return jwtUtil.generateToken("aishwarya", java.util.List.of("ROLE_USER", "ROLE_ADMIN"));

    }

}
