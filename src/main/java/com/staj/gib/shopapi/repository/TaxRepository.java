package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.Tax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaxRepository extends JpaRepository<Tax, UUID> {
    boolean existsByTaxNameIgnoreCase(String taxName);
}