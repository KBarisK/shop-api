package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findAllByCart_Id(UUID cartId);
    Optional<CartItem> findByCart_IdAndProduct_Id(UUID cartId, UUID productId);
}