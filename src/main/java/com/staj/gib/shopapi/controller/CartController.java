package com.staj.gib.shopapi.controller;

import com.staj.gib.shopapi.dto.request.CartRequest;
import com.staj.gib.shopapi.dto.response.CartDto;
import com.staj.gib.shopapi.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/active/{userId}")
    CartDto getActiveCart(@PathVariable @NotNull UUID userId) {
        return this.cartService.getActiveCart(userId);
    }

    @DeleteMapping("/remove-item")
    CartDto removeItemFromCart(@Valid @RequestBody CartRequest cartRequest) {
        return this.cartService.removeItemFromCart(cartRequest);
    }

    @PostMapping("/add-item")
    CartDto addItemToCart(@Valid @RequestBody CartRequest cartRequest) {
        return this.cartService.addItemToCart(cartRequest);
    }

    @DeleteMapping("/remove-all/{cartId}")
    void removeAllItemsFromCart(@PathVariable @NotNull UUID cartId) {
        this.cartService.removeAllItemsFromCart(cartId);
    }

}
