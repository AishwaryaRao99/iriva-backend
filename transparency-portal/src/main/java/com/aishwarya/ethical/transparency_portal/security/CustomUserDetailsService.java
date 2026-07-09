package com.aishwarya.ethical.transparency_portal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aishwarya.ethical.transparency_portal.modules.user.model.CustomUserDetails;
import com.aishwarya.ethical.transparency_portal.modules.user.model.UserModel;
import com.aishwarya.ethical.transparency_portal.modules.user.service.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userService.findByUsernameOrEmail(username);

        return new CustomUserDetails(user);
    }
}
