package com.staj.gib.shopapi.entity.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.staj.gib.shopapi.entity.User}
 */
@Value
public class CreateUserDto implements Serializable {
    @NotNull
    @NotEmpty
    String username;

    @NotNull
    @NotEmpty
    String password;
}