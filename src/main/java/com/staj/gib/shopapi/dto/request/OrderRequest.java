package com.staj.gib.shopapi.dto.request;

import com.staj.gib.shopapi.enums.InstallmentOption;
import com.staj.gib.shopapi.enums.PaymentMethod;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Value
public class OrderRequest implements Serializable {

    @NotNull
    UUID cartId;

    @NotNull
    PaymentMethod paymentMethod;

    @NotNull
    InstallmentOption installmentCount;
}
