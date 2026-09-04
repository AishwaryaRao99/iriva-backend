package com.aishwarya.ethical.transparency_portal.security;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	
	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expirationTime;

	public String generateToken(String username, Collection<String> roles) {
		Date now = new Date();

		Date expiry = new Date(now.getTime() + expirationTime);

		return Jwts.builder()

				.subject(username)
				
				.claim("roles", roles)

				.issuedAt(now)

				.expiration(expiry)

				.signWith(getSigningKey()) // Use the signing key for HMAC SHA-256 aka signature

				.compact(); // convert the JWT to a compact, URL-safe string representation
	}

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	// Extract username/subject from token
	public String getUsernameFromToken(String token) {
		try {
			Claims claims = Jwts.parser()
					.verifyWith(getSigningKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();

			return claims.getSubject();
		} catch (JwtException e) {
			return null;
		}
	}

	// Extract roles claim (assumes it was stored as a Collection<String>)
	public java.util.List<String> getRolesFromToken(String token) {
		try {
			Claims claims = Jwts.parser()
					.verifyWith(getSigningKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();

			Object roles = claims.get("roles");
			if (roles instanceof java.util.List) {
				return (java.util.List<String>) roles;
			}
			return java.util.Collections.emptyList();
		} catch (JwtException | IllegalArgumentException e) {
			return java.util.Collections.emptyList();
		}
	}

	// Validate token signature and expiration
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
					.verifyWith(getSigningKey())
					.build()
					.parseSignedClaims(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}
}
