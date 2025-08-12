package com.staj.gib.shopapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.staj.gib.shopapi.entity.User}
 */
@Value
public class LoginUser implements Serializable {
    @NotNull
    String username;
    @NotNull
    String password;
}