package com.staj.gib.shopapi.entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "installment_payment")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "installment_payment_id")
    private UUID installmentPaymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    // remove installments if parent is gone
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "installmentPayment",
            cascade = CascadeType.REMOVE)
    private List<Installment> installments;

    @Column(name = "interest_rate", precision = 6, scale = 3)
    private BigDecimal interestRate;
}
