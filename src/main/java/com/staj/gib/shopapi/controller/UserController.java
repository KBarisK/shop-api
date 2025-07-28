package com.staj.gib.shopapi.controller;

import com.staj.gib.shopapi.entity.dto.CreateUserDto;
import com.staj.gib.shopapi.entity.dto.ResponseUserDto;
import com.staj.gib.shopapi.entity.dto.UpdateUserDto;
import com.staj.gib.shopapi.exception.InvalidPasswordException;
import com.staj.gib.shopapi.exception.UserNotFoundException;
import com.staj.gib.shopapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/{userId}")
    public ResponseUserDto getUser(@PathVariable UUID userId) throws UserNotFoundException {
        return this.userService.getUser(userId);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseUserDto createUser(@Valid @RequestBody CreateUserDto createUserDto) throws InvalidPasswordException {
         return userService.saveUser(createUserDto);

    }

    @PatchMapping
    public ResponseUserDto updateUser(@Valid @RequestBody UpdateUserDto updateUserDto) throws UserNotFoundException, InvalidPasswordException {
        return this.userService.updateUser(updateUserDto);
    }
}
