package com.staj.gib.shopapi.security;

import com.staj.gib.shopapi.dto.mapper.UserMapper;
import com.staj.gib.shopapi.entity.User;
import com.staj.gib.shopapi.enums.ErrorCode;
import com.staj.gib.shopapi.exception.BusinessException;
import com.staj.gib.shopapi.repository.UserRepository;
import com.staj.gib.shopapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSecurityDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserSecurityDetails(username);
    }

    private UserSecurityDetails getUserSecurityDetails(String username) {
        User user =this.userRepository.findByUsername(username).orElseThrow(()
                -> new BusinessException(ErrorCode.USER_NOT_FOUND, username));

        return userMapper.userToUserSecurityDetails(user);
    }

}
