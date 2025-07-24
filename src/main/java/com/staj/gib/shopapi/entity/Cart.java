package com.staj.gib.shopapi.entity;

import jakarta.persistence.*;
import lombok.*;
import com.staj.gib.shopapi.enums.CartStatus;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cart_id")
    private UUID cartId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name="status")
    private CartStatus status;
}
