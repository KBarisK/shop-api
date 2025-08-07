package com.staj.gib.shopapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", insertable = false, updatable = false)
    private Product product;

    @Column(name="product_id", nullable = false)
    private UUID productId;

    @Column(name = "quantity")
    private short quantity;
}
