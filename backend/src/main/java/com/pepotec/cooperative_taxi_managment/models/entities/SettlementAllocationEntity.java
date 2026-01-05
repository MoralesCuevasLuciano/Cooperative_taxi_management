package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Entidad que representa una asignación de pago para un movimiento de cuenta.
 * 
 * Permite manejar pagos parciales mediante diferentes métodos de pago:
 * - Recibo físico (ReceiptEntity)
 * - Liquidación de sueldo (PayrollSettlementEntity)
 * - Movimiento de dinero (CashMovement o NonCashMovement)
 * 
 * REGLAS DE INMUTABILIDAD:
 * - Si tiene Recibo o Liquidación: 
 *   - INMUTABLE en campos críticos (allocatedAmount, accountMovement, receipt, payrollSettlement, allocationDate)
 *   - MODIFICABLE en campo `note` (permite agregar notas informativas)
 *   - NO ELIMINABLE (no se puede hacer soft delete)
 * - Si tiene Movimiento de Dinero: 
 *   - MODIFICABLE en todos los campos (excepto accountMovement una vez creado)
 *   - ELIMINABLE (cascade delete si se elimina el movimiento)
 */
@Entity
@Table(name = "settlement_allocations", indexes = {
    @Index(name = "idx_account_movement", columnList = "id_account_movement"),
    @Index(name = "idx_receipt", columnList = "id_receipt"),
    @Index(name = "idx_payroll_settlement", columnList = "id_payroll_settlement"),
    @Index(name = "idx_movement", columnList = "id_movement")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementAllocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_settlement_allocation", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account_movement", nullable = true)
    private AbstractAccountMovementEntity accountMovement; // Movimiento que se está saldando

    // XOR: Solo uno de los tres métodos de pago puede estar presente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_receipt", nullable = true)
    private ReceiptEntity receipt; // Recibo que salda (parcial o total)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_payroll_settlement", nullable = true)
    private PayrollSettlementEntity payrollSettlement; // Liquidación que salda (parcial o total)

    /**
     * Movimiento de dinero (CashMovement o NonCashMovement) que salda (parcial o total).
     * 
     * Ahora se puede usar relación JPA directa porque AbstractMovementEntity
     * utiliza estrategia JOINED y es una @Entity real.
     * La eliminación en cascada se maneja manualmente en el servicio de movimientos.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_movement", nullable = true)
    private AbstractMovementEntity movement; // Movimiento de dinero que salda

    @Column(name = "allocated_amount", nullable = false)
    @NotNull(message = "The allocated amount cannot be null")
    @Positive(message = "The allocated amount must be positive")
    private Double allocatedAmount; // Monto asignado de este pago específico

    @Column(name = "allocation_date", nullable = false)
    @NotNull(message = "The allocation date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate allocationDate; // Fecha de asignación

    @Column(name = "note", length = 500)
    private String note; // Nota sobre esta asignación (nullable)

    @Column(name = "active", nullable = false)
    @NotNull(message = "The active status cannot be null")
    @Builder.Default
    private Boolean active = true; // Soft delete
}

