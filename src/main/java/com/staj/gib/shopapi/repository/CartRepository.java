package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}