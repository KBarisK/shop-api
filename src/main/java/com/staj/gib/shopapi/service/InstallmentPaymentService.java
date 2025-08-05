package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.InstallmentPaymentMapper;
import com.staj.gib.shopapi.entity.InstallmentPayment;
import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.repository.InstallmentPaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class InstallmentPaymentService {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final InstallmentPaymentRepository installmentPaymentRepository;
    private final InstallmentPaymentMapper installmentPaymentMapper;

    public void saveInstallmentPayment(Order order, BigDecimal interestRate,int installmentCount) {
        InstallmentPayment installmentPayment = new InstallmentPayment(order,new ArrayList<>(),interestRate);
        InstallmentPayment savedInstallmentPayment =installmentPaymentRepository.save(installmentPayment);
        generateInstallments(savedInstallmentPayment.getId(),order.getTotalAmount(),installmentCount);
    }

    public void generateInstallments(UUID installmentPaymentId, BigDecimal totalAmount, int installmentCount) {
        BigDecimal installmentAmount = totalAmount.divide(BigDecimal.valueOf(installmentCount),SCALE,ROUNDING);
    }

}
