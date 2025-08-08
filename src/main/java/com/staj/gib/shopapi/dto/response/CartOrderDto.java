package com.staj.gib.shopapi.dto.response;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.staj.gib.shopapi.entity.Cart}
 */
@Value
public class CartOrderDto implements Serializable {
    UUID id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    UUID userId;
    List<CartItemDto> cartItems;
}