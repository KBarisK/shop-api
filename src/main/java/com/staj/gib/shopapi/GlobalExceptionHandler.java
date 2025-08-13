package com.staj.gib.shopapi;

import com.staj.gib.shopapi.dto.response.ErrorResponse;
import com.staj.gib.shopapi.enums.ErrorCode;
import com.staj.gib.shopapi.exception.BusinessException;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ErrorResponse buildResponseBody(ErrorCode code, @Nullable Object[] args, HttpServletRequest req){
        return new ErrorResponse(
                code.key(),
                messageSource.getMessage(code.key(), args, LocaleContextHolder.getLocale()),
                req.getRequestURI(),
                OffsetDateTime.now()
        );
    }

    private ResponseEntity<ErrorResponse> handleBusinessExceptionInternal(BusinessException ex,
                                                                          HttpStatusCode code, HttpServletRequest req) {

        ErrorResponse body = buildResponseBody(ex.getCode(), ex.getArgs(), req);

        return ResponseEntity.status(code).body(body);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLockingFailureException(
            OptimisticLockingFailureException ex, HttpServletRequest req) {
        ErrorResponse body = buildResponseBody(ErrorCode.LOCKING_FAILURE, null, req);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,  HttpServletRequest req) {

        logger.error("DataIntegrityViolationException exception occurred", ex);

        ErrorResponse body = buildResponseBody(ErrorCode.DATA_INTEGRITY, null, req);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest req) {

        return handleBusinessExceptionInternal(ex, HttpStatus.BAD_REQUEST, req);
    }


    // this exception is thrown when an argument annotated with @Valid failed validation
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Locale locale = LocaleContextHolder.getLocale();

        List<String> errors = new ArrayList<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            String msg = messageSource.getMessage(fe, locale);
            errors.add(fe.getField() + ": " + msg);
        }
        for (ObjectError oe : ex.getBindingResult().getGlobalErrors()) {
            String msg = messageSource.getMessage(oe, locale);
            errors.add(oe.getObjectName() + ": " + msg);
        }

        BusinessException businessEx = new BusinessException(
                ErrorCode.VALIDATION_ERROR,
                String.join("; ", errors)
        );

        ServletWebRequest servletWebReq = (ServletWebRequest) request;
        ResponseEntity<ErrorResponse> errorResponse = handleBusinessException(businessEx,  servletWebReq.getRequest());
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse.getBody());
    }

    // request is missing a parameter
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        BusinessException businessEx = new BusinessException(
                ErrorCode.MISSING_PARAMETER,
                ex.getParameterName()
        );


        ServletWebRequest servletWebReq = (ServletWebRequest) request;
        ResponseEntity<ErrorResponse> errorResponse = handleBusinessException(businessEx,  servletWebReq.getRequest());
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse.getBody());
    }

    // method argument is not the expected type
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {

        BusinessException businessEx = new BusinessException(
                ErrorCode.PARAMETER_TYPE_MISMATCH,
                ex.getName(),
                Objects.isNull(ex.getRequiredType()) ? "unknown" : ex.getRequiredType().getName()
        );

        ServletWebRequest servletWebReq = (ServletWebRequest) request;
        return  handleBusinessException(businessEx,  servletWebReq.getRequest());
    }

    // this catches validations that fail on controller's @PathVariable, @RequestParam, etc, instead of DTO.
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest req) {

        BusinessException exception = new BusinessException(ErrorCode.VALIDATION_ERROR, ex.getMessage());
        return handleBusinessException(exception, req);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        BusinessException businessEx = new BusinessException(
                ErrorCode.HTTP_METHOD_NOT_SUPPORTED,
                ex.getMethod()
        );

        ServletWebRequest servletWebReq = (ServletWebRequest) request;
        ResponseEntity<ErrorResponse> errorResponse = handleBusinessExceptionInternal(businessEx,
                HttpStatus.METHOD_NOT_ALLOWED, servletWebReq.getRequest());
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse.getBody());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        BusinessException businessEx = new BusinessException(
                ErrorCode.ENDPOINT_NOT_FOUND,
                ex.getHttpMethod(),
                ex.getRequestURL()
        );

        ServletWebRequest servletWebReq = (ServletWebRequest) request;
        ResponseEntity<ErrorResponse> errorResponse =  handleBusinessExceptionInternal(businessEx,
                HttpStatus.NOT_FOUND, servletWebReq.getRequest());
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse.getBody());

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest req) {

        BusinessException businessEx = new BusinessException(
                ErrorCode.ACCESS_DENIED
        );

        return handleBusinessExceptionInternal(businessEx, HttpStatus.FORBIDDEN, req);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest req) {

        ErrorCode errorCode;
        HttpStatus status;

        if (ex instanceof BadCredentialsException) {
            errorCode = ErrorCode.INVALID_CREDENTIALS;
            status = HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof InsufficientAuthenticationException) {
            errorCode = ErrorCode.AUTHENTICATION_REQUIRED;
            status = HttpStatus.UNAUTHORIZED;
        } else {
            errorCode = ErrorCode.AUTHENTICATION_FAILED;
            status = HttpStatus.UNAUTHORIZED;
        }

        BusinessException businessEx = new BusinessException(errorCode);
        return handleBusinessExceptionInternal(businessEx, status, req);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            org.springframework.http.converter.HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        BusinessException businessEx = new BusinessException(
                ErrorCode.HTTP_MESSAGE_NOT_READABLE
        );

        ServletWebRequest servletWebReq = (ServletWebRequest) request;
        ResponseEntity<ErrorResponse> errorResponse = handleBusinessExceptionInternal(
                businessEx, HttpStatus.BAD_REQUEST, servletWebReq.getRequest());

        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse.getBody());
    }


    // fallback for all exceptions
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, WebRequest request) {

        logger.error("Unhandled exception occurred", ex);

        ServletWebRequest servletWebReq = (ServletWebRequest) request;

        ErrorResponse body = buildResponseBody(ErrorCode.UNHANDLED_EXCEPTION, null, servletWebReq.getRequest());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
