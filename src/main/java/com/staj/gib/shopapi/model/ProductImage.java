package com.staj.gib.shopapi.model;
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
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_image_id")
    private UUID productImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @Lob
    @Column(name = "image_url")
    private String imageUrl;
}
