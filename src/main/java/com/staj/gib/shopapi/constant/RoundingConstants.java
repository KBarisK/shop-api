package com.staj.gib.shopapi.constant;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class RoundingConstants {

    public static final int SCALE = 2;
    public static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    public static final BigDecimal DEFAULT_MONTHLY_INTEREST_RATE = BigDecimal.valueOf(0.0425);

}
