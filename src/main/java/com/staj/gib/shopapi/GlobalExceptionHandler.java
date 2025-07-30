package com.staj.gib.shopapi;

import com.staj.gib.shopapi.exception.InvalidPasswordException;
import com.staj.gib.shopapi.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OptimisticLockingFailureException.class)
    // issue HTTP 409
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleOptimisticLockingFailureException(OptimisticLockingFailureException ex) {
        return "The resource was modified by another user. Please refresh and try again.";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // todo check the reason?
        return ex.getMessage();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    // issue HTTP 404
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String entityNotFoundHandler(ResourceNotFoundException ex) {

        return ex.getMessage();
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidPassword(InvalidPasswordException ex) {
        return ex.getMessage();
    }


}
