package com.staj.gib.shopapi.dto;

import jakarta.validation.constraints.*;
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
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{6,255}$",
            message = "Password must be 6-20 characters and include at least one digit," +
                    " one lowercase letter, one uppercase letter, and one special character.")
    String password;
}