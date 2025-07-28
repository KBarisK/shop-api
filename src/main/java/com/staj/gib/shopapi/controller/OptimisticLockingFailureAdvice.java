package com.staj.gib.shopapi.controller;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice  // signals that this advice is rendered straight into the response body.
public class OptimisticLockingFailureAdvice {
    @ExceptionHandler(OptimisticLockingFailureException.class)
    // issue HTTP 409
    @ResponseStatus(HttpStatus.CONFLICT)
    String optimisticLockingFailureHandler(OptimisticLockingFailureException ex) {
        return "The resource was modified by another user. Please refresh and try again.";
    }
}
