package com.staj.gib.shopapi.dto.request;

import com.staj.gib.shopapi.entity.ProductImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link ProductImage}
 */
@Value
public class PayInstallmentRequest implements Serializable {
    @NotNull
    UUID installmentId;

}