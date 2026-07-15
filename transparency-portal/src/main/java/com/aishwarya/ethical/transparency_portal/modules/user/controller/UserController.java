package com.aishwarya.ethical.transparency_portal.modules.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aishwarya.ethical.transparency_portal.exception_handling.BadRequestException;
import com.aishwarya.ethical.transparency_portal.modules.user.dto.UserRequest;
import com.aishwarya.ethical.transparency_portal.modules.user.service.UserService;

import jakarta.validation.Valid;

//got to check later
@RestController
public class UserController {
	public UserController(UserService userService) {
	}

	@PostMapping
	public String createUser(@Valid @RequestBody UserRequest request) {
		if ("test".equalsIgnoreCase(request.getName())) {
			throw new BadRequestException("Name 'test' is not allowed");
		}
		return "User created!";
	}
}
