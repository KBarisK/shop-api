package com.staj.gib.shopapi.dto.response;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class CartItemDto implements Serializable {
    UUID id;
    ProductResponse product;
    short quantity;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
