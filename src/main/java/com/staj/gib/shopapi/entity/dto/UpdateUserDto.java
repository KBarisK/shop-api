package com.staj.gib.shopapi.entity.dto;

import com.staj.gib.shopapi.entity.User;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link User}
 */
@Value
public class UpdateUserDto implements Serializable {
    UUID id;
    String username;
    String password;
}