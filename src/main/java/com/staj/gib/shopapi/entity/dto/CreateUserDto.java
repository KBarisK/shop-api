package com.staj.gib.shopapi.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.staj.gib.shopapi.entity.User}
 */
@Value
public class CreateUserDto implements Serializable {
    @NotBlank
    @Size(max=50)
    String username;

    @NotBlank
    @Size(max=255)
    String password;
}