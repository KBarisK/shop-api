package com.staj.gib.shopapi.entity.dto;

import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class CartItemDto implements Serializable {
    UUID id;
    UUID productId;
    BigDecimal price;
    BigDecimal preTaxPrice;
    short quantity;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
