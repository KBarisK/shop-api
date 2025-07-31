package com.staj.gib.shopapi.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class CategoryTaxRequest {
    @NotNull(message = "Tax ID is required")
    UUID taxId;

    @NotNull(message = "Tax percentage is required")
    @DecimalMin(value = "0.000", message = "Tax percentage must be non-negative")
    @DecimalMax(value = "100.000", message = "Tax percentage cannot exceed 100%")
    @Digits(integer = 3, fraction = 3, message = "Tax percentage must have at most 3 integer digits and 3 decimal places")
    BigDecimal taxPercent;
}