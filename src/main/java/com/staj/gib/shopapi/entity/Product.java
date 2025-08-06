package com.staj.gib.shopapi.entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity
{
    @Column(name = "product_name", length = 50, nullable = false)
    private String productName;

    @Column(name = "price", precision = 18, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id", insertable = false, updatable = false)
    private ProductCategory category;

    @Column(name="category_id", nullable = false)
    private UUID categoryId;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ProductImage> images;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "stock")
    private Integer stock;
}
