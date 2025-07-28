package com.staj.gib.shopapi.entity.dto;

import com.staj.gib.shopapi.entity.Tax;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.staj.gib.shopapi.entity.Tax}
 */
@Value  // similar to @Data but immutable
public class TaxResponse implements Serializable {
    UUID id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String taxName;

    public static TaxResponse fromEntity(Tax tax){
        return new TaxResponse(tax.getId(), tax.getCreatedAt(), tax.getUpdatedAt(), tax.getTaxName());
    }
}