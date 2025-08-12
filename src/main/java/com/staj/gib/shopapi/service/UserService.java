package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.UserMapper;
import com.staj.gib.shopapi.dto.request.CreateUserDto;
import com.staj.gib.shopapi.dto.request.LoginUser;
import com.staj.gib.shopapi.dto.request.UpdateUserDto;
import com.staj.gib.shopapi.dto.response.ResponseUserDto;
import com.staj.gib.shopapi.dto.response.UserResponse;
import com.staj.gib.shopapi.entity.User;
import com.staj.gib.shopapi.enums.ErrorCode;
import com.staj.gib.shopapi.enums.UserType;
import com.staj.gib.shopapi.exception.BusinessException;
import com.staj.gib.shopapi.repository.UserRepository;
import com.staj.gib.shopapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final CartService cartService;
    private final PasswordEncoder passwordEncoder;

    public ResponseUserDto getUser(UUID userID) {
        User user = userRepository.findById(userID).orElseThrow(()
                -> new BusinessException(ErrorCode.USER_NOT_FOUND, userID));
        return userMapper.userToResponseUserDto(user);
    }
    
    @Transactional
    public ResponseUserDto updateUser(UpdateUserDto updateUserDto) {
        User user = userRepository.findById(updateUserDto.getId()).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND, updateUserDto.getId()));
        user = userMapper.updateUserDtoToUser(updateUserDto, user, passwordEncoder);
        User updatedUser = userRepository.save(user);
        return userMapper.userToResponseUserDto(updatedUser);
    }

    @Transactional
    public UserResponse register(CreateUserDto createUserDto) {
        User user = userMapper.createUserDtoToUser(createUserDto, passwordEncoder);
        user.setUserType(UserType.CUSTOMER);
        User savedUser = userRepository.save(user);

        this.cartService.createCart(savedUser.getId());
        String token = this.jwtService.generateToken(savedUser.getUsername());
        return userMapper.userToUserResponse(savedUser,token);
    }

    public UserResponse login(LoginUser loginUser) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword())
        );

        User user = userRepository.findByUsername(loginUser.getUsername()).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND, loginUser.getUsername()));

        return userMapper.userToUserResponse(user,jwtService.generateToken(loginUser.getUsername()));
    }


}

