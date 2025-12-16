package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * Entidad que representa el saldo acumulado de combustible pendiente de reintegro
 * para un chofer. Este saldo se acumula del porcentaje del chofer en cada DailyFuel
 * y se reintegra quincenalmente al balance de la MemberAccount.
 */
@Entity
@Table(name = "fuel_reimbursements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelReimbursementEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fuel_reimbursement", unique = true, nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_member_account", nullable = false, unique = true)
    @NotNull(message = "The member account cannot be null")
    private MemberAccountEntity memberAccount;

    @Column(name = "accumulated_amount", nullable = false)
    @NotNull(message = "The accumulated amount cannot be null")
    private Double accumulatedAmount;

    @Column(name = "last_reimbursement_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate lastReimbursementDate;

    @Column(name = "created_date", nullable = false)
    @NotNull(message = "The created date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;

    @Column(name = "active", nullable = false)
    @NotNull(message = "The active status cannot be null")
    @Builder.Default
    private Boolean active = true;
}

