package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "advances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AdvanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_advance", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_member_account", nullable = false)
    @NotNull(message = "The member account cannot be null")
    private MemberAccountEntity memberAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_payroll_settlement")
    private PayrollSettlementEntity payrollSettlement; // nullable, se asocia al liquidar

    @Column(name = "movement_id")
    private Long movementId; // CashMovement o NonCashMovement (guardamos ID genérico)

    @Column(name = "date", nullable = false)
    @NotNull(message = "The date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @Column(name = "amount", nullable = false)
    @NotNull(message = "The amount cannot be null")
    private Double amount;

    @Column(name = "notes")
    private String notes;

    @Column(name = "active", nullable = false)
    @NotNull(message = "The active status cannot be null")
    private Boolean active = true;
}


