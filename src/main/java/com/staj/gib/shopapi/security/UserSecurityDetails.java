package com.staj.gib.shopapi.security;

import com.staj.gib.shopapi.enums.UserType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserSecurityDetails implements UserDetails {

    private UUID id;
    private String username;
    private String password;
    private UserType userType;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = "ROLE_" + userType.name();
        return List.of(new SimpleGrantedAuthority(roleName));
    }
}
