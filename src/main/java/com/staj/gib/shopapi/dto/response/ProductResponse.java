package com.staj.gib.shopapi.dto.response;

import lombok.Value;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Value
public class ProductResponse implements Serializable {
    UUID id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String productName;
    BigDecimal price;
    String description;
    Integer stock;
    UUID categoryId;
    List<ProductImageResponse> images;
}
