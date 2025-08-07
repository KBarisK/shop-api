package com.staj.gib.shopapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Value
public class  CreateProductRequest implements Serializable {

    @NotBlank(message = "Product name is required")
    @Size(max = 50, message = "Product name must not exceed 50 characters")
    String productName;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 18, fraction = 2, message = "Price must have maximum 18 integer digits and 2 decimal places")
    BigDecimal price;

    @NotNull(message = "Category ID is required")
    UUID categoryId;

    @Valid
    String description;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    Integer stock;

    @Valid
    List<ProductImageRequest> imageUrls;
}