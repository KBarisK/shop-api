package com.staj.gib.shopapi.dto.request;

import com.staj.gib.shopapi.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitialOrderRequest {
    private UUID userId;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
}
