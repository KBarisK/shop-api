package com.staj.gib.shopapi.exception;

import com.staj.gib.shopapi.enums.ErrorCode;


public class BusinessException extends RuntimeException {
    private final ErrorCode code;
    private final Object[] args;

    public BusinessException(ErrorCode code, Object... args) {
        super(code.name());
        this.code = code;
        this.args = args;
    }
    public ErrorCode getCode(){ return code; }
    public Object[] getArgs(){ return args; }
}
