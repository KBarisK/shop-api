package com.staj.gib.shopapi.dto.request;

import com.staj.gib.shopapi.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Value
public class OrderRequest implements Serializable {

    @NotBlank
    UUID cartId;

    @NotBlank
    PaymentMethod paymentMethod;
}
