package com.staj.gib.shopapi.dto.request;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Value
public class CartRepuest implements Serializable {
    UUID cartId;
    UUID productId;
    short quantity;
}
