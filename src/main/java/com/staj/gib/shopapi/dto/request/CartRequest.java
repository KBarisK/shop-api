package com.staj.gib.shopapi.dto.request;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Value
public class CartRequest implements Serializable {
    UUID userId;
    UUID productId;
    short quantity;
}
