package com.staj.gib.shopapi.controller;

import com.staj.gib.shopapi.dto.request.CashOrderRequest;
import com.staj.gib.shopapi.dto.request.InstallmentOrderRequest;
import com.staj.gib.shopapi.dto.request.PayInstallmentRequest;
import com.staj.gib.shopapi.dto.response.InstallmentDto;
import com.staj.gib.shopapi.dto.response.InstallmentPaymentDto;
import com.staj.gib.shopapi.dto.response.OrderResponse;
import com.staj.gib.shopapi.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/installment")
    public OrderResponse placeInstallmentOrder(@Valid @RequestBody InstallmentOrderRequest orderRequest) {
        return orderService.placeInstallmentOrder(orderRequest);
    }

    @PostMapping("/cash")
    public OrderResponse placeCashOrder(@Valid @RequestBody CashOrderRequest orderRequest) {
        return orderService.placeCashOrder(orderRequest);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable @NotNull UUID orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> getOrdersOfUser(@PathVariable @NotNull UUID userId) {
        return orderService.getOrdersOfUser(userId);
    }


}
