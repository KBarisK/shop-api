package com.staj.gib.shopapi.dto.response;

import lombok.Value;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Value
public class CategoryResponse {
    UUID id;
    String categoryName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<CategoryTaxResponse> taxes;
}