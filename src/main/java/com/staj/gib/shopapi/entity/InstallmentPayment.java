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
public class InstallmentPayment extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", updatable = false, insertable = false)
    private Order order;

    @Column(name="order_id",nullable = false)
    private UUID orderId;

    // remove installments if parent is gone
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "installmentPayment",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Installment> installments;

    @Column(name = "interest_rate", precision = 6, scale = 3)
    private BigDecimal interestRate;
}
