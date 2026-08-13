package com.aishwarya.ethical.transparency_portal.security;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.aishwarya.ethical.transparency_portal.modules.user.model.UserModel;
import com.aishwarya.ethical.transparency_portal.modules.user.service.OAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final OAuth2UserService oAuth2UserService;
	private final JWTUtil jwtUtil;
	private final ObjectMapper objectMapper;

	@Value("${oauth2.redirect-url:http://localhost:5173}")
	private String redirectUrl;

	public OAuth2SuccessHandler(OAuth2UserService oAuth2UserService, JWTUtil jwtUtil, ObjectMapper objectMapper) {
		this.oAuth2UserService = oAuth2UserService;
		this.jwtUtil = jwtUtil;
		this.objectMapper = objectMapper;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		try {
			if (authentication instanceof OAuth2AuthenticationToken) {
				OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
				OAuth2User oAuth2User = oAuth2Token.getPrincipal();

				// Extract user information from OAuth2 principal
				String email = oAuth2User.getAttribute("email");				
				log.info("OAuth2 authentication successful for user: {}", email);
				String name = oAuth2User.getAttribute("name");
				String providerId = oAuth2User.getAttribute("sub"); // Google uses 'sub' for unique ID

				// Check if user exists, if not create one
				UserModel user = oAuth2UserService.findOrCreateUser(email, name, providerId);

				// Generate JWT token with user roles
				Collection<String> roles = extractRoles(user.getRole());
				String jwt = jwtUtil.generateToken(email, roles);

				log.info("JWT token generated for OAuth2 user: {}", email);

				// Set JWT as HTTP-only cookie with secure settings
				ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
						.httpOnly(true)
						.secure(false) // Set to true in production with HTTPS
						.path("/")
						.maxAge(Duration.ofHours(1))
						.sameSite("Lax") // In production with different domains, use "None" and set secure=true
						.build();

				response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

				// Send response with user information
				//sendAuthenticationResponse(response, jwt, user);
				
				//redirect to react app
				getRedirectStrategy().sendRedirect(request, response, redirectUrl);
			}
		} catch (Exception ex) {
			log.error("OAuth2 authentication failed: {}", ex.getMessage(), ex);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication failed");
		}
	}

	/**
	 * Extract roles from user role string (e.g., "ROLE_USER" -> ["ROLE_USER"])
	 */
	private Collection<String> extractRoles(String roleString) {
		if (roleString != null && !roleString.trim().isEmpty()) {
			return Collections.singletonList(roleString);
		}
		return Collections.singletonList("ROLE_USER");
	}

	/**
	 * Send authentication response with user details to frontend
	 */
	private void sendAuthenticationResponse(HttpServletResponse response, String jwt, UserModel user)
			throws IOException {
		
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);

		Map<String, Object> body = Map.of(
			"success", true,
			"message", "OAuth2 authentication successful",
			"user", Map.of(
				"id", user.getId(),
				"email", user.getEmail(),
				"username", user.getUsername(),
				"role", user.getRole()
			),
			"token", jwt
		);

		response.getWriter().write(objectMapper.writeValueAsString(body));
	}
}
