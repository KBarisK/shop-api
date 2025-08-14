package com.staj.gib.shopapi.controller;

import com.staj.gib.shopapi.dto.request.PayInstallmentRequest;
import com.staj.gib.shopapi.dto.response.InstallmentPaymentDto;
import com.staj.gib.shopapi.service.InstallmentService;
import com.staj.gib.shopapi.service.InstallmentPaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/installment")
@AllArgsConstructor
public class InstallmentController {
    private final InstallmentService installmentService;
    private final InstallmentPaymentService installmentPaymentService;

    @GetMapping("/order/{orderId}")
    public InstallmentPaymentDto getInstallmentsByOrder(@PathVariable @NotNull UUID orderId) {
        return installmentPaymentService.getInstallmentsByOrder(orderId);
    }

    @PostMapping("/pay")
    public InstallmentPaymentDto payInstallment(
            @Valid @RequestBody PayInstallmentRequest request) {
        return installmentService.payInstallment(request);
    }

}
