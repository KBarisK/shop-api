package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.InstallmentPaymentMapper;
import com.staj.gib.shopapi.dto.response.InstallmentPaymentDto;
import com.staj.gib.shopapi.entity.InstallmentPayment;
import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.repository.InstallmentPaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class InstallmentPaymentService {

    private final InstallmentPaymentRepository installmentPaymentRepository;
    private final InstallmentPaymentMapper installmentPaymentMapper;

    public InstallmentPaymentDto saveInstallmentPayment(Order order, BigDecimal interestRate) {
        InstallmentPayment installmentPayment = new InstallmentPayment(order,new ArrayList<>(),interestRate);
        return this.installmentPaymentMapper.toDto(installmentPaymentRepository.save(installmentPayment));
    }

}
