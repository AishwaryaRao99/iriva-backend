package com.aishwarya.ethical.transparency_portal.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aishwarya.ethical.transparency_portal.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12); // Strength of 12 for BCrypt encryption
	}

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
    
	@Autowired
	private com.aishwarya.ethical.transparency_portal.security.CustomUserDetailsService customUserDetailsService;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()) // Disabled for stateless REST API
				.authorizeHttpRequests(auth -> auth
						// ========== PUBLIC ENDPOINTS (No authentication required) ==========

						// Authentication endpoints - Allow unauthenticated login
						.requestMatchers("/auth/**").permitAll()

						// Product API endpoints - Allow public product browsing
						.requestMatchers("/api/v1/productsapi/**").permitAll()

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

				.headers(headers -> headers.frameOptions(frame -> frame.disable()))

				// Stateless session management because we use JWTs
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
				// Register the authentication provider (uses UserDetailsService + PasswordEncoder)
				.authenticationProvider(authenticationProvider())
                
				;

		// Add the JWT filter before Spring Security's username/password filter
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
}
