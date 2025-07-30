package com.staj.gib.shopapi.dto;

import com.staj.gib.shopapi.entity.CartItem;
import lombok.Value;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Value
public class CartDto implements Serializable {

    UUID id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<CartItem> cartItems;
}
