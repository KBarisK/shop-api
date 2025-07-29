package com.staj.gib.shopapi;

import com.staj.gib.shopapi.exception.*;
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
    public String optimisticLockingFailureHandler(OptimisticLockingFailureException ex) {
        return "The resource was modified by another user. Please refresh and try again.";
    }

    @ExceptionHandler(TaxNotFoundException.class)
    // issue HTTP 404
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String taxNotFoundHandler(TaxNotFoundException ex) {

        return ex.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFound(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidPassword(InvalidPasswordException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCartNotFound(CartNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CartItemNotFoundExcepiton.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCartItemNotFound(CartItemNotFoundExcepiton ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductNotFound(ProductNotFoundException ex) {
        return ex.getMessage();
    }
}
