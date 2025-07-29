package com.staj.gib.shopapi.entity;

import jakarta.persistence.*;
import lombok.*;
import com.staj.gib.shopapi.enums.CartStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cart")
    private List<CartItem> cartItems;
}
