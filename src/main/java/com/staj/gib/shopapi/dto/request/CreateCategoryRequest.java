// Create request
package com.staj.gib.shopapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Value;

import java.util.List;

@Value
public class CreateCategoryRequest {
    @NotBlank(message = "Category name is required")
    @Size(max = 20, message = "Category name must not exceed 20 characters")
    String categoryName;

    @Valid
    List<CategoryTaxRequest> taxes;
}