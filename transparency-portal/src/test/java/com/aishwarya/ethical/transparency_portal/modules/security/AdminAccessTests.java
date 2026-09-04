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
}
