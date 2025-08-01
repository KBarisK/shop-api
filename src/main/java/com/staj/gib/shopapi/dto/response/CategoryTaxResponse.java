package com.staj.gib.shopapi.dto.response;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Value
public class CategoryTaxResponse {
    UUID taxId;
    String taxName;
    BigDecimal taxPercent;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}