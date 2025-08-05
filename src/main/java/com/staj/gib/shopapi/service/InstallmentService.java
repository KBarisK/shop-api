package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.entity.Order;
import com.staj.gib.shopapi.repository.InstallmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class InstallmentService {

    private final InstallmentRepository installmentRepository;

    public void generateInstallments(Order order, int installmentMonths, BigDecimal interestRate) {
        BigDecimal totalAmount = order.getTotalAmount();

    }
}
