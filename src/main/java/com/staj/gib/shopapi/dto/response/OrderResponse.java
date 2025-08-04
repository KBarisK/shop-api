package com.staj.gib.shopapi.dto.response;

import com.staj.gib.shopapi.enums.OrderStatus;
import com.staj.gib.shopapi.enums.PaymentMethod;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.staj.gib.shopapi.entity.Order}
 */
@Value
public class OrderResponse implements Serializable {
    UUID id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    OrderStatus status;
    BigDecimal totalAmount;
    PaymentMethod paymentMethod;
}