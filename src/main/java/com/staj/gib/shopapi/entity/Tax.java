package com.staj.gib.shopapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "tax")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Tax extends BaseEntity{
    @Column(name = "tax_name", length = 50, nullable = false, unique=true)
    private String taxName;
}
