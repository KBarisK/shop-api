package com.staj.gib.shopapi.entity;

import com.staj.gib.shopapi.enums.OrderStatus;
import com.staj.gib.shopapi.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "\"order\"")  // order is reserved in postgresql
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity{
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "status")
    private OrderStatus status;

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
