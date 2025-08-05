package com.staj.gib.shopapi.dto.response;

import com.staj.gib.shopapi.enums.InstallmentStatus;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.staj.gib.shopapi.entity.Installment}
 */
@Value
public class InstallmentDto implements Serializable {
    UUID id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    BigDecimal amount;
    LocalDateTime dueDate;
    LocalDateTime paidAt;
    BigDecimal lateFee;
    InstallmentStatus status;
}