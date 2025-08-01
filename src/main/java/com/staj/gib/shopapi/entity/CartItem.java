package com.staj.gib.shopapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name = "price", precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "pre_tax_price", precision = 18, scale = 2)
    private BigDecimal preTaxPrice;

    @Column(name = "quantity")
    private short quantity;
}
