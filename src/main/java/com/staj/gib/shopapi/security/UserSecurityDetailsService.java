package com.staj.gib.shopapi.security;

import com.staj.gib.shopapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSecurityDetailsService implements UserDetailsService {
    private UserService userService;

    @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserSecurityDetails(username);
    }

    private UserSecurityDetails getUserSecurityDetails(String username) {
   return this.userService.getUserDetails(username);
    }

}
