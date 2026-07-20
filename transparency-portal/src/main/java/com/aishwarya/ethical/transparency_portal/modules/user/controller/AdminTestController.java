package com.aishwarya.ethical.transparency_portal.modules.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminTestController {

    @GetMapping("/test")
    public String testAdminAccess() {
        return "Admin access granted";
    }
}
