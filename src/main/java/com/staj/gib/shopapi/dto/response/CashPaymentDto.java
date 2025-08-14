package com.staj.gib.shopapi.dto.response;

import com.staj.gib.shopapi.entity.CashPayment;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link CashPayment}
 */
@Value
public class CashPaymentDto implements Serializable {
    UUID id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    UUID orderId;
}