package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.CashPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CashPaymentRepository extends JpaRepository<CashPayment, UUID> {
}