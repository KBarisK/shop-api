package com.staj.gib.shopapi.entity;

import com.staj.gib.shopapi.enums.CartStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity {
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="status")
    private CartStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;
}
