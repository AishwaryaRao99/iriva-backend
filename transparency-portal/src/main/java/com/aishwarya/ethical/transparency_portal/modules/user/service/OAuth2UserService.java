package com.aishwarya.ethical.transparency_portal.modules.user.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aishwarya.ethical.transparency_portal.modules.user.model.UserModel;
import com.aishwarya.ethical.transparency_portal.modules.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OAuth2UserService {

	private final UserRepository userRepository;

	public OAuth2UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Find existing user by email or create a new one if doesn't exist
	 * This handles the OAuth2 login flow for Google authentication
	 * 
	 * @param email Google user's email
	 * @param name Google user's name
	 * @param providerId Google's unique identifier (sub claim)
	 * @return Existing or newly created UserModel
	 */
	public UserModel findOrCreateUser(String email, String name, String providerId) {
		log.info("OAuth2: Looking up user with email: {}", email);

		// Try to find existing user by email
		Optional<UserModel> existingUser = userRepository.findByEmail(email);

		if (existingUser.isPresent()) {
			log.info("OAuth2: User found with email: {}", email);
			UserModel user = existingUser.get();
			// Update the last login timestamp
			user.setUpdatedAt(LocalDateTime.now());
			return userRepository.save(user);
		}

		// User doesn't exist, create a new one
		log.info("OAuth2: Creating new user with email: {}", email);
		return createOAuth2User(email, name, providerId);
	}

	/**
	 * Create a new user from OAuth2 credentials
	 * 
	 * @param email Google user's email
	 * @param name Google user's name
	 * @param providerId Google's unique identifier (sub claim)
	 * @return Newly created UserModel
	 */
	private UserModel createOAuth2User(String email, String name, String providerId) {
		UserModel newUser = new UserModel();

		// Generate username from email (use email prefix before @)
		String username = email.split("@")[0];

		// Ensure unique username by appending provider ID if needed
		if (userRepository.existsByUsername(username)) {
			username = username + "_" + providerId.substring(0, Math.min(6, providerId.length()));
		}

		newUser.setUsername(username);
		newUser.setDisplayName(name);
		newUser.setEmail(email);

		// OAuth2 users don't have a password, set to null or a placeholder
		// The JWT filter will handle OAuth2 authenticated users differently
		newUser.setPassword(null);

		// Set default role to USER
		newUser.setRole("ROLE_USER");

		// Set timestamps
		LocalDateTime now = LocalDateTime.now();
		newUser.setCreatedAt(now);
		newUser.setUpdatedAt(now);

		// Save the new user
		UserModel savedUser = userRepository.save(newUser);
		log.info("OAuth2: New user created successfully - Email: {}, Username: {}, ID: {}", email, username,
				savedUser.getId());

		return savedUser;
	}

	/**
	 * Find user by email
	 * 
	 * @param email User's email
	 * @return Optional containing the user if found
	 */
	public Optional<UserModel> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * Find user by username
	 * 
	 * @param username User's username
	 * @return Optional containing the user if found
	 */
	public Optional<UserModel> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
