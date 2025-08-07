package com.staj.gib.shopapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Value
public class UpdateProductRequest implements Serializable {
    @NotBlank
    UUID id;

    @Size(max = 50, message = "Product name must not exceed 50 characters")
    String productName;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 18, fraction = 2, message = "Price must have maximum 18 integer digits and 2 decimal places")
    BigDecimal price;

    UUID categoryId;

    String description;

    @Min(value = 0, message = "Stock cannot be negative")
    Integer stock;

    @Valid
    List<ProductImageRequest> imageUrls;
}
