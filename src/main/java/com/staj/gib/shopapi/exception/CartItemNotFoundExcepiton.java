package com.staj.gib.shopapi.exception;

public class CartItemNotFoundExcepiton extends RuntimeException {
    public CartItemNotFoundExcepiton() {
        super("CartItem not found for cart id and product id ");
    }
}
