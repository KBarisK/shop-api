package com.staj.gib.shopapi.controller;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice  // signals that this advice is rendered straight into the response body.
public class TaxNotFoundAdvice {
    // configures the advice to only respond when an EmployeeNotFoundException is thrown.
    @ExceptionHandler(TaxNotFoundException.class)
    // issue HTTP 404
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String taxNotFoundHandler(TaxNotFoundException ex) {
        return ex.getMessage();
    }
}
