package com.staj.gib.shopapi.dto.response;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class ErrorResponse {
    String code;
    String message;
    String path;
    OffsetDateTime timestamp;
}
