package com.staj.gib.shopapi.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_name", length = 50, nullable = false)
    private String productName;

    @Column(name = "price", precision = 18, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private ProductCategory category;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "stock")
    private Integer stock;

    @Lob
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Integer version;
}
