package com.staj.gib.shopapi.dto.response;

import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.staj.gib.shopapi.entity.OrderItem}
 */
@Value
public class OrderItemDto implements Serializable {
    UUID id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    UUID productId;
    short quantity;
    BigDecimal price;
    BigDecimal preTaxPrice;
}