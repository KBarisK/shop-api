package com.staj.gib.shopapi.dto.response;

import lombok.Value;

@Value
public class UserResponse {
    String token;
    ResponseUserDto user;
}
