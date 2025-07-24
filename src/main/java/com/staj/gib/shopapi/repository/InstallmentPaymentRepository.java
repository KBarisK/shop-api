package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.InstallmentPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstallmentPaymentRepository extends JpaRepository<InstallmentPayment, UUID> {
}