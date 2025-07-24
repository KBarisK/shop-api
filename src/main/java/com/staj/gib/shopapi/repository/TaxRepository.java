package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.Tax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaxRepository extends JpaRepository<Tax, UUID> {
}