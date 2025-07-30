package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.entity.User;
import com.staj.gib.shopapi.entity.dto.CreateUserDto;
import com.staj.gib.shopapi.entity.dto.ResponseUserDto;
import com.staj.gib.shopapi.entity.dto.UpdateUserDto;
import com.staj.gib.shopapi.exception.InvalidPasswordException;
import com.staj.gib.shopapi.exception.NotFoundException;
import com.staj.gib.shopapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.staj.gib.shopapi.enums.UserType;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseUserDto getUser(UUID userID) throws NotFoundException {
        return userRepository.findById(userID)
                .map(user -> new ResponseUserDto(
                        user.getId(),
                        user.getCreatedAt(),
                        user.getUpdatedAt(),
                        user.getVersion(),
                        user.getUsername(),
                        user.getUserType()
                )).orElseThrow(() -> new NotFoundException("User not found with id: " + userID));
    }

    public ResponseUserDto saveUser(CreateUserDto createUserDto) throws InvalidPasswordException {
        if (isPasswordInvalid(createUserDto.getPassword())) {
            throw new InvalidPasswordException("Password does not meet security requirements");
        }

        User user = new User();
        user.setUsername(createUserDto.getUsername());
        user.setPassword(createUserDto.getPassword());
        user.setUserType(UserType.CUSTOMER);
        User savedUser = this.userRepository.save(user);
        return new ResponseUserDto(
                savedUser.getId(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt(),
                savedUser.getVersion(),
                savedUser.getUsername(),
                savedUser.getUserType()
        );
    }

    public ResponseUserDto updateUser(UpdateUserDto updateUserDto) {
        if(isPasswordInvalid(updateUserDto.getPassword())){
            throw new InvalidPasswordException("Password does not meet security requirements");
        }

        User user = userRepository.findById(updateUserDto.getId()).orElseThrow(
                () -> new NotFoundException("User not found with id: " + updateUserDto.getId()));
        user.setUsername(updateUserDto.getUsername());
        user.setPassword(updateUserDto.getPassword());
        User updatedUser = userRepository.save(user);
        return new ResponseUserDto(
                updatedUser.getId(),
                updatedUser.getCreatedAt(),
                updatedUser.getUpdatedAt(),
                updatedUser.getVersion(),
                updatedUser.getUsername(),
                updatedUser.getUserType()
        );
    }

    private boolean isPasswordInvalid(String password){
        //String regExp = "^(?=.[0-9])(?=.[a-z])(?=.[A-Z])(?=.[@#).{6,20}$";

        final String SPECIAL_CHARACTERS = "@#$%^&+=";
        final int MIN_LENGTH = 6;

        if (password == null || password.length() < MIN_LENGTH) {
            return true;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;



        for (char c : password.toCharArray()) {
            if (Character.isWhitespace(c)){
                return true;
            }
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            }
            else if (Character.isLowerCase(c)) {
                hasLower = true;
            }
            else if (Character.isDigit(c)) {
                hasDigit = true;
            }
            else if (SPECIAL_CHARACTERS.indexOf(c) >= 0) {
                hasSpecial = true;
            }
        }

        return !hasUpper || !hasLower || !hasDigit || !hasSpecial;
    }
}

