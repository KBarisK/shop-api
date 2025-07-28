package com.staj.gib.shopapi.exception;

import java.util.UUID;

public class TaxNotFoundException extends RuntimeException {
    public TaxNotFoundException(UUID id) {
        super("Could not find tax " + id);
    }
}
