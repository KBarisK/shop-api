package com.staj.gib.shopapi.dto.request;

import com.staj.gib.shopapi.enums.InstallmentOption;
import com.staj.gib.shopapi.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Value
public class InstallmentOrderRequest implements Serializable {

    @NotNull
    UUID cartId;

    @NotNull
    InstallmentOption installmentCount;
}
