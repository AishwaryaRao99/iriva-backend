package com.aishwarya.ethical.transparency_portal.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.aishwarya.ethical.transparency_portal.security.JwtAuthenticationEntryPoint;
import com.aishwarya.ethical.transparency_portal.security.JwtAuthenticationFilter;
import com.aishwarya.ethical.transparency_portal.security.OAuth2SuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12); // Strength of 12 for BCrypt encryption
	}

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtAuthenticationEntryPoint authenticationEntryPoint;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthenticationEntryPoint authenticationEntryPoint, OAuth2SuccessHandler oAuth2SuccessHandler) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.oAuth2SuccessHandler = oAuth2SuccessHandler;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()) // Disabled for stateless REST API
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests(auth -> auth
						// ========== PUBLIC ENDPOINTS (No authentication required) ==========

						// Authentication endpoints - Allow unauthenticated login
						.requestMatchers("/auth/**").permitAll()

						// Product API endpoints - Allow public product browsing
						.requestMatchers("/api/v1/productsapi/**").permitAll()

						// Profile data and user actions require the logged-in user
						.requestMatchers("/api/v1/profile/**").authenticated()

						// Test endpoints - Allow JWT token generation for testing
						.requestMatchers("/api/test/**").permitAll()

						// for h2 console access - dev purposes only -- change this in prod
						.requestMatchers("/h2-console/**").permitAll()

						// ========== ROLE-BASED PROTECTED ENDPOINTS ==========

						// Admin role endpoints
						.requestMatchers("/admin/**").hasRole("ADMIN")

						// Client role endpoints
						.requestMatchers("/client/**").hasRole("CLIENT")

						// User role endpoints
						.requestMatchers("/user/**").hasRole("USER")

						// ========== DEFAULT SECURITY RULE ==========
						// All other requests require authentication
						.anyRequest().authenticated())
				
				.exceptionHandling(exception -> exception
					    .authenticationEntryPoint(authenticationEntryPoint))

				.headers(headers -> headers.frameOptions(frame -> frame.disable()))

				// OAuth2 Login Configuration - for "Continue with Google"
				.oauth2Login(oauth2 -> oauth2
						.successHandler(oAuth2SuccessHandler))

				// Stateless session management because we use JWTs
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))                
				;

		// Add the JWT filter before Spring Security's username/password filter
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManager(
	        AuthenticationConfiguration configuration)
	        throws Exception {

	    return configuration.getAuthenticationManager();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

	    CorsConfiguration configuration = new CorsConfiguration();

	    configuration.setAllowedOrigins(
	            List.of("http://localhost:5173"));

	    configuration.setAllowedMethods(
	            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

	    configuration.setAllowedHeaders(
	            List.of("*"));

	    configuration.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source =
	            new UrlBasedCorsConfigurationSource();

	    source.registerCorsConfiguration("/**", configuration);

	    return source;
	}
}