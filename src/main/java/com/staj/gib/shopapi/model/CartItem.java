package com.staj.gib.shopapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cart_item_id")
    private UUID cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cartID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product productID;

    @Column(name = "price", precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "quantity")
    private short quantity;
}
