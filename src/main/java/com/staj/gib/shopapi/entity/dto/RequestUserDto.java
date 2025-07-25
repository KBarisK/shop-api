package com.staj.gib.shopapi.entity.dto;

import com.staj.gib.shopapi.entity.User;
import com.staj.gib.shopapi.enums.UserType;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link User}
 */
@Value
public class RequestUserDto implements Serializable {
    UUID id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Integer version;
    String username;
    UserType userType;
}