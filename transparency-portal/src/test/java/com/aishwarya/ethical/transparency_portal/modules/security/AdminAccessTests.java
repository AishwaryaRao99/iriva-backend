package com.aishwarya.ethical.transparency_portal.modules.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.aishwarya.ethical.transparency_portal.security.JWTUtil;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
class AdminAccessTests {
	
	@Autowired
	JWTUtil jwtUtil;
	
	@Autowired
	MockMvc mockMvc;
	
	@Test
	void adminShouldAccessAdminEndpoint() throws Exception {

	    String token = jwtUtil.generateToken("admin", List.of("ROLE_ADMIN"));

	    Cookie jwtCookie = new Cookie("jwt", token);

	    mockMvc.perform(get("/admin/test")
	            .cookie(jwtCookie))
	            .andExpect(status().isOk());
	}
	
	@Test
	void userShouldNotAccessAdminEndpoint() throws Exception {

	    String token = jwtUtil.generateToken("user", List.of("ROLE_USER"));

	    Cookie jwtCookie = new Cookie("jwt", token);

	    mockMvc.perform(get("/admin/test")
	            .cookie(jwtCookie))
	            .andExpect(status().isForbidden());
	}
	
	@Test
	void anonymousShouldReceive401() throws Exception {

	    mockMvc.perform(get("/admin/test"))
	            .andExpect(status().isUnauthorized());
	}

	@Test
	void actuatorRequiresAdminRoleAndHealthRemainsAvailable() throws Exception {
		String userToken = jwtUtil.generateToken("user", List.of("ROLE_USER"));
		Cookie userJwtCookie = new Cookie("jwt", userToken);
		mockMvc.perform(get("/actuator/health").cookie(userJwtCookie))
				.andExpect(status().isForbidden());

		String adminToken = jwtUtil.generateToken("admin", List.of("ROLE_ADMIN"));
		Cookie adminJwtCookie = new Cookie("jwt", adminToken);
		mockMvc.perform(get("/actuator/health").cookie(adminJwtCookie))
				.andExpect(status().isOk());
		mockMvc.perform(get("/actuator/prometheus").cookie(adminJwtCookie))
				.andExpect(status().isNotFound());
	}

	@Test
	void actuatorRejectsAnonymousRequests() throws Exception {
		mockMvc.perform(get("/actuator/health"))
				.andExpect(status().isUnauthorized());
	}
}
