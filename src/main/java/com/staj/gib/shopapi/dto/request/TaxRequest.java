package com.staj.gib.shopapi.dto.request;

import com.staj.gib.shopapi.entity.Tax;
import lombok.*;

import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

  // similar to @Data but immutable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxRequest implements Serializable {
    // not null and size > 0
    @NotBlank(message = "Tax name is required")
    @Size(max = 50, message = "Tax name must not exceed 50 characters")
    private String taxName;
}