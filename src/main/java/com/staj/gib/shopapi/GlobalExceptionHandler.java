package com.staj.gib.shopapi;

import com.staj.gib.shopapi.dto.response.ErrorResponse;
import com.staj.gib.shopapi.exception.BusinessException;
import com.staj.gib.shopapi.exception.InvalidPasswordException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Locale;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

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

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest req) {
        Locale locale = LocaleContextHolder.getLocale();
        String localized = messageSource.getMessage(ex.getCode().key(), ex.getArgs(), locale);

        ErrorResponse body = new ErrorResponse(
                ex.getCode().name(),
                localized,
                req.getRequestURI(),
                OffsetDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidPassword(InvalidPasswordException ex) {
        return ex.getMessage();
    }


    /// validasyon mesajlarınaformat verilip buraya entegrasyonu yazılacak @Valid ile tetiklenenler için


}
