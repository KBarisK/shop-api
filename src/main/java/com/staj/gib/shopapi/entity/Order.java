package com.staj.gib.shopapi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.staj.gib.shopapi.enums.OrderStatus;
import com.staj.gib.shopapi.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "\"order\"")  // order is reserved in postgresql
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity{
    @Column(name="user_id",nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id" , updatable = false,insertable = false)
    private User user;

    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "total_amount", precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems;
}
