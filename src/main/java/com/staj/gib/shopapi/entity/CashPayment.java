package com.staj.gib.shopapi.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Table(name = "cash_payment")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CashPayment extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", updatable = false, insertable = false)
    private Order order;

    @Column(name="order_id",nullable = false)
    private UUID orderId;
}
