package com.staj.gib.shopapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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
    @JoinColumn(name = "product_id")
    private Product product;

    short quantity;

    @Column(name = "price", precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "pre_tax_price", precision = 18, scale = 2)
    private BigDecimal preTaxPrice;

}
