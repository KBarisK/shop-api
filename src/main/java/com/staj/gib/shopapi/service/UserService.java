package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.entity.User;
import com.staj.gib.shopapi.dto.request.CreateUserDto;
import com.staj.gib.shopapi.dto.response.ResponseUserDto;
import com.staj.gib.shopapi.dto.request.UpdateUserDto;
import com.staj.gib.shopapi.dto.mapper.UserMapper;
import com.staj.gib.shopapi.exception.InvalidPasswordException;
import com.staj.gib.shopapi.exception.ResourceNotFoundException;
import com.staj.gib.shopapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.staj.gib.shopapi.enums.UserType;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final CartService cartService;

    public ResponseUserDto getUser(UUID userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new ResourceNotFoundException("User",userID));
        return userMapper.userToResponseUserDto(user);
    }

    public ResponseUserDto saveUser(CreateUserDto createUserDto) throws InvalidPasswordException {
        User user = userMapper.createUserDtoToUser(createUserDto);
        user.setUserType(UserType.CUSTOMER);
        User savedUser = userRepository.save(user);
        this.cartService.createCart(savedUser.getId());
        return userMapper.userToResponseUserDto(savedUser);
    }

    public ResponseUserDto updateUser(UpdateUserDto updateUserDto) {
        User user = userRepository.findById(updateUserDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", updateUserDto.getId()));
        user = userMapper.updateUserDtoToUser(updateUserDto, user);
        User updatedUser = userRepository.save(user);
        return userMapper.userToResponseUserDto(updatedUser);
    }

}

