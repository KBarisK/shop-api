package com.staj.gib.shopapi.controller;

import com.staj.gib.shopapi.dto.request.OrderRequest;
import com.staj.gib.shopapi.dto.response.OrderResponse;
import com.staj.gib.shopapi.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }

    @GetMapping("/{orderId}")
    public OrderResponse processOrder(@PathVariable UUID orderId) {
        return orderService.getOrder(orderId);
    }
}
