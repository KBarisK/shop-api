package com.staj.gib.shopapi.controller;

import java.util.UUID;

public class TaxNotFoundException extends RuntimeException {
    public TaxNotFoundException(UUID id) {
        super("Could not find tax " + id);
    }
}
