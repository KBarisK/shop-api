package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstallmentRepository extends JpaRepository<Installment, UUID> {
}