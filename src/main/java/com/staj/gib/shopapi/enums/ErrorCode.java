package com.staj.gib.shopapi.enums;


public enum ErrorCode {

    USER_NOT_FOUND("E001"),
    TAX_NOT_FOUND("E002"),
    PRODUCT_NOT_FOUND("E003"),
    ORDER_NOT_FOUND("E004"),
    PRODUCT_CATEGORY_NOT_FOUND("E005"),
    CART_NOT_FOUND("E006"),
    CART_FOR_USER_NOT_FOUND("E007");


    private final String key;
    ErrorCode(String key){ this.key = key; }
    public String key(){ return key; }
}