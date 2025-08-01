package com.staj.gib.shopapi.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_category")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory extends BaseEntity
{
    @Column(name = "category_name", length = 20, nullable = false,  unique = true)
    private String categoryName;

    @OneToMany(
            mappedBy = "category",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductCategoryTax> taxes;

}
