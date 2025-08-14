package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.InstallmentPaymentMapper;
import com.staj.gib.shopapi.dto.response.InstallmentPaymentDto;
import com.staj.gib.shopapi.dto.response.OrderResponse;
import com.staj.gib.shopapi.entity.Installment;
import com.staj.gib.shopapi.entity.InstallmentPayment;
import com.staj.gib.shopapi.enums.ErrorCode;
import com.staj.gib.shopapi.enums.InstallmentStatus;
import com.staj.gib.shopapi.exception.BusinessException;
import com.staj.gib.shopapi.repository.InstallmentPaymentRepository;
import com.staj.gib.shopapi.service.validator.InstallmentPaymentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InstallmentPaymentService {
    private final InstallmentPaymentRepository installmentPaymentRepository;
    private final InstallmentPaymentMapper installmentPaymentMapper;


    public InstallmentPaymentDto getInstallmentsByOrder(UUID orderId) {
        InstallmentPayment installmentPayment = installmentPaymentRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_ORDER_TYPE, orderId));

        return installmentPaymentMapper.toDto(installmentPayment);
    }

    @Transactional
    public void saveInstallmentPayment(OrderResponse order, BigDecimal interestRate, int installmentCount) {
        InstallmentPayment installmentPayment = new InstallmentPayment();
        installmentPayment.setOrderId(order.getId());
        installmentPayment.setInstallments(new ArrayList<>());
        installmentPayment.setInterestRate(interestRate);

        InstallmentPayment savedInstallmentPayment = installmentPaymentRepository.save(installmentPayment);
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
