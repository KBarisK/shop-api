package com.staj.gib.shopapi.entity.dto;

import com.staj.gib.shopapi.entity.Tax;
import lombok.Value;
import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Value  // similar to @Data but immutable
public class TaxRequest implements Serializable {
    // not null and size > 0
    @NotBlank(message = "Tax name is required")
    @Size(max = 50, message = "Tax name must not exceed 50 characters")
    String taxName;

    public Tax toEntity(){
        return new Tax(taxName);
    }
}