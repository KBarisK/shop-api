package com.staj.gib.shopapi.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("Could not find entity with " + id);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
