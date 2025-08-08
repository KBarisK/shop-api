package com.staj.gib.shopapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @OneToOne
    @JoinColumn(name = "product_id", updatable = false, insertable = false)
    private Product product;

    @Column(name="product_id",nullable = false)
    private UUID productId;

    short quantity;

    @Column(name = "price", precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "pre_tax_price", precision = 18, scale = 2)
    private BigDecimal preTaxPrice;

}
