package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.entity.Installment;
import com.staj.gib.shopapi.entity.InstallmentPayment;
import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.enums.InstallmentStatus;
import com.staj.gib.shopapi.repository.InstallmentPaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InstallmentPaymentService {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final InstallmentPaymentRepository installmentPaymentRepository;

    public void saveInstallmentPayment(Order order, BigDecimal interestRate,int installmentCount) {
        InstallmentPayment installmentPayment = new InstallmentPayment(order,new ArrayList<>(),interestRate);
        InstallmentPayment savedInstallmentPayment =installmentPaymentRepository.save(installmentPayment);
        generateInstallments(savedInstallmentPayment,order.getTotalAmount(),installmentCount);
    }

    private void generateInstallments(InstallmentPayment installmentPayment, BigDecimal totalAmount, int installmentCount) {

        BigDecimal installmentAmount = totalAmount.divide(java.math.BigDecimal.valueOf(installmentCount), SCALE, ROUNDING);
        
        // Handle rounding remainder for the last installment
        BigDecimal remainingAmount = totalAmount;
        List<Installment> installments = new ArrayList<>();
        
        // Generate installments
        for (int i = 1; i <= installmentCount; i++) {
            Installment installment = new Installment();
            
            // For the last installment, use the remaining amount to handle rounding differences
            BigDecimal currentInstallmentAmount = (i == installmentCount) 
                ? remainingAmount 
                : installmentAmount;
            
            installment.setAmount(currentInstallmentAmount);
            installment.setInstallmentPayment(installmentPayment);
            installment.setDueDate(LocalDateTime.now().plusMonths(i));
            installment.setStatus(InstallmentStatus.UNPAID);
            installment.setLateFee(BigDecimal.ZERO);
            
            installments.add(installment);
            remainingAmount = remainingAmount.subtract(currentInstallmentAmount);
        }

        installmentPayment.setInstallments(installments);
        installmentPaymentRepository.save(installmentPayment);
    }

}
