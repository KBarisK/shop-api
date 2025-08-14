package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.InstallmentPaymentMapper;
import com.staj.gib.shopapi.dto.request.PayInstallmentRequest;
import com.staj.gib.shopapi.dto.response.InstallmentPaymentDto;
import com.staj.gib.shopapi.entity.Installment;
import com.staj.gib.shopapi.entity.InstallmentPayment;
import com.staj.gib.shopapi.enums.ErrorCode;
import com.staj.gib.shopapi.enums.InstallmentStatus;
import com.staj.gib.shopapi.exception.BusinessException;
import com.staj.gib.shopapi.repository.InstallmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InstallmentService {
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
