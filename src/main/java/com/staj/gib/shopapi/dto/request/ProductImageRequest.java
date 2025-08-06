package com.staj.gib.shopapi.dto.request;

import com.staj.gib.shopapi.entity.ProductImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link ProductImage}
 */
@Value
public class ProductImageRequest implements Serializable {
    @NotNull
    @NotEmpty
    @NotBlank
    String imageUrl;
}