package com.staj.gib.shopapi.exception;

import java.util.UUID;

public class CartItemNotFound extends RuntimeException {
    public CartItemNotFound() {
        super("CartItem not found for cart id and product id ");
    }
}
