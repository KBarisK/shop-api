package com.staj.gib.shopapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product_category_tax")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryTax extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private ProductCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tax_id", insertable = false, updatable = false)
    private Tax tax;

    @Column(name="tax_id", nullable = false)
    private UUID taxId;

    @Column(name = "tax_percent", precision = 6, scale = 3)
    private BigDecimal taxPercent;
}
