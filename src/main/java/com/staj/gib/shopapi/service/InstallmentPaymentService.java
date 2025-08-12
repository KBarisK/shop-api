package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.entity.Installment;
import com.staj.gib.shopapi.entity.InstallmentPayment;
import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.enums.InstallmentStatus;
import com.staj.gib.shopapi.repository.InstallmentPaymentRepository;
import com.staj.gib.shopapi.service.validator.InstallmentPaymentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InstallmentPaymentService {

    private final InstallmentPaymentRepository installmentPaymentRepository;

    @Transactional
    public void saveInstallmentPayment(Order order, BigDecimal interestRate,int installmentCount) {
        InstallmentPayment installmentPayment = new InstallmentPayment(order,new ArrayList<>(),interestRate);
        InstallmentPayment savedInstallmentPayment =installmentPaymentRepository.save(installmentPayment);
        generateInstallments(savedInstallmentPayment,order.getTotalAmount(),installmentCount);
    }

    private void generateInstallments(InstallmentPayment installmentPayment, BigDecimal totalAmount, int installmentCount) {
        List<BigDecimal> amounts = InstallmentPaymentValidator.calculateInstallmentAmounts(totalAmount, installmentCount);
        List<Installment> installments = new ArrayList<>();

        for (int i = 0; i < installmentCount; i++) {
            Installment installment = new Installment();
            installment.setAmount(amounts.get(i));
            installment.setInstallmentPayment(installmentPayment);
            installment.setDueDate(LocalDateTime.now().plusMonths(i + 1));
            installment.setStatus(InstallmentStatus.UNPAID);
            installment.setLateFee(BigDecimal.ZERO);
            installments.add(installment);
        }

        installmentPayment.setInstallments(installments);
        installmentPaymentRepository.save(installmentPayment);
    }

}
