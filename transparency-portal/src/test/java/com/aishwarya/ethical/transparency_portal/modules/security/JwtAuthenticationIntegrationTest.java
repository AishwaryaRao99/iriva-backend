package com.aishwarya.ethical.transparency_portal.modules.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import jakarta.servlet.http.Cookie;;

@SpringBootTest
@AutoConfigureMockMvc
class JwtAuthenticationIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void protectedEndpoint_withJwtCookie_returns200() throws Exception {

		String request = """
				{
				    "username":"john_doe",
				    "password":"password"
				}
				""";

		MvcResult loginResult = mockMvc
				.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isOk()).andExpect(cookie().exists("jwt")).andReturn();

		Cookie jwtCookie = loginResult.getResponse().getCookie("jwt");

		mockMvc.perform(get("/user/test").cookie(jwtCookie)).andExpect(status().isOk());
	}
}
