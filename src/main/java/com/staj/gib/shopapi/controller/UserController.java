package com.staj.gib.shopapi.controller;

import com.staj.gib.shopapi.entity.dto.CreateUserDto;
import com.staj.gib.shopapi.entity.dto.RequestUserDto;
import com.staj.gib.shopapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/")
    public ResponseEntity<RequestUserDto> getUser(UUID userId) {
        return this.userService.getUser(userId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<RequestUserDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        return this.userService.saveUser(createUserDto).map(userDto -> ResponseEntity
                        .created(URI.create("/user/" + userDto.getId()))
                        .body(userDto))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PatchMapping
    public ResponseEntity<RequestUserDto> updateUser(@Valid @RequestBody RequestUserDto requestUserDto) {
        return new ResponseEntity<>(requestUserDto, HttpStatus.OK);
    }
}
