package com.staj.gib.shopapi.exception;

import java.util.UUID;

public class CartNotFound extends RuntimeException{
    public CartNotFound(UUID id) {super("Could not find cart " + id);}
}
