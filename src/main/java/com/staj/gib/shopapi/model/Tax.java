package com.staj.gib.shopapi.model;

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
public class Tax {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tax_id")
    private UUID taxId;

    @Column(name = "tax_name", length = 50, nullable = false)
    private String taxName;
}
