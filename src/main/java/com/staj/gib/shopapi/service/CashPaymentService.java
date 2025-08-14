package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.dto.mapper.CashPaymentMapper;
import com.staj.gib.shopapi.dto.response.CashPaymentDto;
import com.staj.gib.shopapi.dto.response.OrderResponse;
import com.staj.gib.shopapi.entity.CashPayment;
import com.staj.gib.shopapi.repository.CashPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CashPaymentService {
    private final CashPaymentRepository cashPaymentRepository;
    private final CashPaymentMapper cashPaymentMapper;

    @Transactional
    public CashPaymentDto handleCashPayment(UUID orderId){
        CashPayment cashPayment = new CashPayment();
        cashPayment.setOrderId(orderId);
        return cashPaymentMapper.toDto(cashPaymentRepository.save(cashPayment));
    }
}
