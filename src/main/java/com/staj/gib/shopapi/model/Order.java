package com.staj.gib.shopapi.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "\"order\"")  // order is reserved in postgresql
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    // active happens when there are pending installments
    public enum Status {
        ACTIVE,
        FINISHED,
    }

    public enum PaymentMethod {
        PAYMENT_CASH,
        PAYMENT_INSTALLMENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "status")
    private Status status;

    @Column(name = "total_amount", precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    // remove CashPayment if order gets removed
    @OneToOne(mappedBy = "order",
            cascade = CascadeType.REMOVE)
    private CashPayment cashPayment;

    // remove InstallmentPayment if order gets removed.
    @OneToOne(mappedBy = "order",
            cascade = CascadeType.REMOVE)
    private InstallmentPayment installmentPayment;
}
