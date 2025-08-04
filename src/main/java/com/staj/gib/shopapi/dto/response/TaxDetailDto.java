package com.staj.gib.shopapi.dto.response;

import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

@Value
public class TaxDetailDto implements Serializable {
    String name;
    BigDecimal amount;
}