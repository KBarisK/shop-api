package com.staj.gib.shopapi.dto.response;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class ProductImageResponse {
    UUID id;
    String imageUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}