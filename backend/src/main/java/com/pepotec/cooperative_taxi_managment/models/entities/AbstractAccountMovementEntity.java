package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Clase base abstracta para movimientos de cuenta que NO afectan el saldo hasta que se "agregan".
 * 
 * Esta clase utiliza la estrategia JOINED para la herencia, donde las clases hijas
 * (AccountIncomeEntity y AbstractAccountExpenseEntity) tienen sus propias tablas.
 * 
 * El campo `added` indica que el movimiento fue agregado al saldo de la cuenta
 * (como ingreso o egreso según la clase), independientemente de si está completamente pagado o no.
 */
@Entity
@Table(name = "account_movements")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractAccountMovementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_account_movement", unique = true, nullable = false)
    private Long id;

    // Solo una cuenta puede estar presente (MemberAccount, SubscriberAccount o VehicleAccount)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_member_account", nullable = true)
    private MemberAccountEntity memberAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subscriber_account", nullable = true)
    private SubscriberAccountEntity subscriberAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehicle_account", nullable = true)
    private VehicleAccountEntity vehicleAccount;

    @Column(name = "amount", nullable = false)
    @NotNull(message = "The amount cannot be null")
    @Positive(message = "The amount must be positive")
    private Double amount; // Siempre positivo, el signo lo define Income vs Expense

    /**
     * Campo que almacena el período devengado (año-mes) del movimiento en formato "YYYY-MM".
     * 
     * Representa el mes al que corresponde el movimiento, aunque se pague después.
     * Ejemplo: una cuota de Diciembre que se paga en Enero tiene yearMonth = "2024-12"
     */
    @Column(name = "period", nullable = false, length = 7)
    @NotNull(message = "The period (yearMonth) cannot be null")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "The period must be in format YYYY-MM (e.g., 2024-12)")
    private String yearMonth;

    @Column(name = "added", nullable = false)
    @NotNull(message = "The added status cannot be null")
    @Builder.Default
    private Boolean added = false; // Si ya fue agregado al saldo general

    @Column(name = "added_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate addedDate; // Fecha en que se agregó al saldo (nullable)

    @Column(name = "note", length = 500)
    private String note; // Nota/descripción informativa (nullable)

    @Column(name = "current_installment")
    private Integer currentInstallment; // Número de cuota actual (nullable, solo para AccountIncome y MonthlyExpense)

    @Column(name = "final_installment")
    private Integer finalInstallment; // Número de cuota final (nullable, solo para AccountIncome y MonthlyExpense)

    @Column(name = "active", nullable = false)
    @NotNull(message = "The active status cannot be null")
    @Builder.Default
    private Boolean active = true; // Soft delete
}

