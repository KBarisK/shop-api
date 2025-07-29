package com.staj.gib.shopapi.exception;

import java.util.UUID;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(UUID id) {super("Could not find cart " + id);}
}
