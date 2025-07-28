package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.Cart;
import com.staj.gib.shopapi.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUser_IdAndStatus(UUID id, CartStatus status);
}