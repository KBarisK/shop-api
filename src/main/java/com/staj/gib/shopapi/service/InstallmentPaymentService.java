package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.InstallmentPaymentMapper;
import com.staj.gib.shopapi.dto.request.PayInstallmentRequest;
import com.staj.gib.shopapi.dto.response.InstallmentPaymentDto;
import com.staj.gib.shopapi.dto.response.OrderResponse;
import com.staj.gib.shopapi.entity.Installment;
import com.staj.gib.shopapi.entity.InstallmentPayment;
import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.enums.ErrorCode;
import com.staj.gib.shopapi.enums.InstallmentStatus;
import com.staj.gib.shopapi.enums.PaymentMethod;
import com.staj.gib.shopapi.exception.BusinessException;
import com.staj.gib.shopapi.repository.InstallmentPaymentRepository;
import com.staj.gib.shopapi.repository.InstallmentRepository;
import com.staj.gib.shopapi.service.validator.InstallmentPaymentValidator;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InstallmentPaymentService {
    private final InstallmentRepository installmentRepository;
    private final InstallmentPaymentMapper installmentPaymentMapper;
    private final OrderService orderService;

    @Transactional
    public InstallmentPaymentDto payInstallment(PayInstallmentRequest request){
        UUID installmentId = request.getInstallmentId();
        Installment installment = installmentRepository.findById(installmentId)
                .orElseThrow(()-> new BusinessException(ErrorCode.INSTALLMENT_NOT_FOUND, installmentId));
        if(installment.getStatus() == InstallmentStatus.PAID){
            throw new BusinessException(ErrorCode.INSTALLMENT_ALREADY_PAID, installmentId);
        }

        InstallmentPayment installmentPayment = installment.getInstallmentPayment();
        installment.setStatus(InstallmentStatus.PAID);
        installment.setPaidAt(LocalDateTime.now());

        boolean allPaid = installmentPayment.getInstallments().stream().allMatch((Installment i)
                -> i.getStatus() == InstallmentStatus.PAID);
        if(allPaid){
            orderService.markOrderFinished(installmentPayment.getOrderId());
        }

        return installmentPaymentMapper.toDto(installmentPayment);
    }
}
