package com.staj.gib.shopapi.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "installment")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Installment {

    public enum Status {
        PAID,
        UNPAID,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "installment_id")
    private UUID installmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="installment_payment_id")
    private InstallmentPayment installmentPayment;

    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "late_fee", precision = 18, scale = 2)
    private BigDecimal lateFee;

    @Column(name = "status")
    private Status status;
}
