package com.staj.gib.shopapi.controller;

import com.staj.gib.shopapi.dto.request.OrderRequest;
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

    @PostMapping("/place")
    public OrderResponse placeOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }

    @GetMapping("/{orderId}")
    public OrderResponse processOrder(@PathVariable @NotNull UUID orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> getOrdersOfUser(@PathVariable @NotNull UUID userId) {
        return orderService.getOrdersOfUser(userId);
    }
}
