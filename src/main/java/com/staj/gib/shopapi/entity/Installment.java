package com.staj.gib.shopapi.entity;
import jakarta.persistence.*;
import lombok.*;
import com.staj.gib.shopapi.enums.InstallmentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "installment")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Installment extends BaseEntity{
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
    private InstallmentStatus status;
}
