package com.staj.gib.shopapi.exception;

import java.util.UUID;

public class ProductNotFound extends RuntimeException {
    public ProductNotFound(UUID id) {super("Could not find product " + id);}
}
