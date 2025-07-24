package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.CashPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CashPaymentRepository extends JpaRepository<CashPayment, UUID> {
}