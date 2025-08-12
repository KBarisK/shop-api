package com.staj.gib.shopapi.service.validator;

import com.staj.gib.shopapi.constant.RoundingConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InstallmentPaymentValidator {

    public static List<BigDecimal> calculateInstallmentAmounts(BigDecimal totalAmount, int installmentCount) {
        BigDecimal installmentAmount = totalAmount.divide(
                BigDecimal.valueOf(installmentCount),
                RoundingConstants.SCALE,
                RoundingConstants.ROUNDING
        );

        BigDecimal remainingAmount = totalAmount;
        List<BigDecimal> amounts = new ArrayList<>();

        for (int i = 1; i <= installmentCount; i++) {
            BigDecimal currentInstallmentAmount = (i == installmentCount)
                    ? remainingAmount
                    : installmentAmount;

            amounts.add(currentInstallmentAmount);
            remainingAmount = remainingAmount.subtract(currentInstallmentAmount);
        }

        return amounts;
    }
}
