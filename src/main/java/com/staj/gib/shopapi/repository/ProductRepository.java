package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}