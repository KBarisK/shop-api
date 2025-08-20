package com.staj.gib.shopapi.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "product_image")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name = "image_url", length = 500, nullable = false)
    private String imageUrl;

    @Column(name = "public_id", length = 255, nullable = false)
    private String publicId;
}
