package com.aishwarya.ethical.transparency_portal.modules.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;;

@SpringBootTest
@AutoConfigureMockMvc
class JwtAuthenticationIntegrationTest {
	
	@Autowired
	MockMvc mockMvc;

	@Test
	void login_returnsJwt() throws Exception {

	    String request = """
	        {
	            "username":"john",
	            "password":"password"
	        }
	        """;

	    mockMvc.perform(post("/auth/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(request))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.token").exists());
	}
}
