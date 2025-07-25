package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.ProductCategoryTax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductCategoryTaxRepository extends JpaRepository<ProductCategoryTax, UUID> {
}