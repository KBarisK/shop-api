package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}