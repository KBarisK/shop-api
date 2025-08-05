package com.staj.gib.shopapi.dto.response;

import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.staj.gib.shopapi.entity.InstallmentPayment}
 */
@Value
public class InstallmentPaymentDto implements Serializable {
    UUID id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<InstallmentDto> installments;
    BigDecimal interestRate;
}