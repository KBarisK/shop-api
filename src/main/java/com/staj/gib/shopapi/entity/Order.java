package com.staj.gib.shopapi.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import com.staj.gib.shopapi.enums.OrderStatus;
import com.staj.gib.shopapi.enums.PaymentMethod;

@Entity
@Table(name = "\"order\"")  // order is reserved in postgresql
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "total_amount", precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    // remove CashPayment if order gets removed
    @OneToOne(mappedBy = "order",
            cascade = CascadeType.REMOVE)
    private CashPayment cashPayment;

    // remove InstallmentPayment if order gets removed.
    @OneToOne(mappedBy = "order",
            cascade = CascadeType.REMOVE)
    private InstallmentPayment installmentPayment;
}
