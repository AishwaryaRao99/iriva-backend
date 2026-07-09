package com.aishwarya.ethical.transparency_portal.modules.user.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

	private final UserModel user;
	
	 public CustomUserDetails(UserModel user) {
	        this.user = user;
	    }

	public Long getId() {
		return user.getId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(
	            new SimpleGrantedAuthority(user.getRole())
	        );
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		 return user.getUsername();
	}

}
