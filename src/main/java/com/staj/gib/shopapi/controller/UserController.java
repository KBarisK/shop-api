package com.staj.gib.shopapi.controller;

import com.staj.gib.shopapi.dto.request.CreateUserDto;
import com.staj.gib.shopapi.dto.request.LoginUser;
import com.staj.gib.shopapi.dto.request.UpdateUserDto;
import com.staj.gib.shopapi.dto.response.ResponseUserDto;
import com.staj.gib.shopapi.dto.response.UserResponse;
import com.staj.gib.shopapi.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/{userId}")
    public ResponseUserDto getUser(@PathVariable @NotNull UUID userId)
    {
        return this.userService.getUser(userId);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseUserDto createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        return userService.saveUser(createUserDto);

    }

    @PatchMapping
    public ResponseUserDto updateUser(@Valid @RequestBody UpdateUserDto updateUserDto) {
        return this.userService.updateUser(updateUserDto);
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginUser loginUser) {
        return this.userService.login(loginUser);
    }

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody CreateUserDto createUserDto) {
        return this.userService.register(createUserDto);
    }
}
