package com.staj.gib.shopapi.enums;


public enum ErrorCode {

    USER_NOT_FOUND("E001"),
    TAX_NOT_FOUND("E002"),
    PRODUCT_NOT_FOUND("E003"),
    ORDER_NOT_FOUND("E004"),
    PRODUCT_CATEGORY_NOT_FOUND("E005"),
    CART_NOT_FOUND("E006"),
    CART_FOR_USER_NOT_FOUND("E007"),
    INVALID_ORDER_TYPE("E008"),


    // API validation error codes
    VALIDATION_ERROR("V001"),
    MISSING_PARAMETER("V002"),
    PARAMETER_TYPE_MISMATCH("V003"),
    HTTP_METHOD_NOT_SUPPORTED("V004"),
    UNHANDLED_EXCEPTION("V005"),
    LOCKING_FAILURE("V006"),
    DATA_INTEGRITY("V007"),
    ENDPOINT_NOT_FOUND("V008"),
    HTTP_MESSAGE_NOT_READABLE("V009"),

    // AUTH related error codes
    ACCESS_DENIED("A001"),
    INVALID_CREDENTIALS("A002"),
    AUTHENTICATION_REQUIRED("A003"),
    AUTHENTICATION_FAILED("A004");



    private final String key;
    ErrorCode(String key){ this.key = key; }
    public String key(){ return key; }
}