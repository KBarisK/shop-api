package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.Cart;
import com.staj.gib.shopapi.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUser_IdAndStatus(UUID id, CartStatus status);
}