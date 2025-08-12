package com.staj.gib.shopapi.dto.request;

import com.staj.gib.shopapi.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link User}
 */
@Value
public class UpdateUserDto implements Serializable {
    @NotBlank
    UUID id;

    @Size(max=50)
    String username;

    @Size(max=255)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=?/!£()]).{6,255}$",
            message = "Password must be 6-20 characters and include at least one digit," +
                    " one lowercase letter, one uppercase letter, and one special character.")
    String password;
}