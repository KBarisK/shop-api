package com.staj.gib.shopapi.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InstallmentOption {
    THREE(3),
    SIX(6),
    TWELVE(12),
    TWENTY_FOUR(24);

    private final int value;

    InstallmentOption(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
