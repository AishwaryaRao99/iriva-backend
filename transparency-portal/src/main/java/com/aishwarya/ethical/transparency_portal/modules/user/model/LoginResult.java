package com.aishwarya.ethical.transparency_portal.modules.user.model;

import com.aishwarya.ethical.transparency_portal.modules.auth.dto.LoginResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult {
	private String jwt; // internal use
	private LoginResponse loginResponse; // or username

	public static LoginResult of(String jwtToken, LoginResponse loginResponse) {
		return new LoginResult(jwtToken, loginResponse);
	}
}
