package com.staj.gib.shopapi.entity;
import jakarta.persistence.*;
import lombok.*;
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
    @Column(name = "category_name", length = 20, nullable = false)
    private String categoryName;



}
