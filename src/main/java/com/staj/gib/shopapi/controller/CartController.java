package com.staj.gib.shopapi.controller;

import com.staj.gib.shopapi.dto.request.CartRepuest;
import com.staj.gib.shopapi.dto.response.CartDto;
import com.staj.gib.shopapi.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/active/{userId}")
    CartDto getActiveCart(@PathVariable UUID userId) {
        return this.cartService.getActiveCart(userId);
    }

    @DeleteMapping("/remove-item")
    void removeItemFromCart(CartRepuest cartRepuest) {
        this.cartService.removeItemFromCart(cartRepuest);
    }

    @PostMapping("/add-item")
    void addItemToCart(CartRepuest cartRepuest) {
        this.cartService.addItemToCart(cartRepuest);
    }

    @DeleteMapping("/remove-all/{cartId}")
    void removeAllItemsFromCart(@PathVariable UUID cartId) {
        this.cartService.removeAllItemsFromCart(cartId);
    }

}
