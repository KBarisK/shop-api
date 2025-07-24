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
    @JoinColumn(name="order_id")
    private Order order;
}
