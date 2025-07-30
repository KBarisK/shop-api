package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.ProductCategoryTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductCategoryTaxRepository extends JpaRepository<ProductCategoryTax, UUID> {
}