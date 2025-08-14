package com.staj.gib.shopapi.controller;

import com.staj.gib.shopapi.dto.request.CashOrderRequest;
import com.staj.gib.shopapi.dto.request.InstallmentOrderRequest;
import com.staj.gib.shopapi.dto.request.PayInstallmentRequest;
import com.staj.gib.shopapi.dto.response.InstallmentDto;
import com.staj.gib.shopapi.dto.response.InstallmentPaymentDto;
import com.staj.gib.shopapi.dto.response.OrderResponse;
import com.staj.gib.shopapi.service.InstallmentPaymentService;
import com.staj.gib.shopapi.service.InstallmentService;
import com.staj.gib.shopapi.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/installment")
@AllArgsConstructor
public class InstallmentController {
    private final InstallmentPaymentService installmentPaymentService;
    private final InstallmentService installmentService;

    @GetMapping("/order/{orderId}")
    public InstallmentPaymentDto getInstallmentsByOrder(@PathVariable @NotNull UUID orderId) {
        return installmentService.getInstallmentsByOrder(orderId);
    }

    @PostMapping("/pay")
    public InstallmentPaymentDto payInstallment(
            @Valid @RequestBody PayInstallmentRequest request) {
        return installmentPaymentService.payInstallment(request);
    }

}
