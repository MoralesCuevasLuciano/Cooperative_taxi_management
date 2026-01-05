package com.pepotec.cooperative_taxi_managment.models.entities;

import com.pepotec.cooperative_taxi_managment.models.enums.RepairType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un arreglo de taller.
 * 
 * Extiende AbstractAccountExpenseEntity y utiliza estrategia JOINED.
 * El amount es positivo pero representa un egreso (se resta del saldo cuando se agrega).
 * 
 * NO puede tener cuotas (currentInstallment y finalInstallment deben ser null).
 * El remainingBalance se actualiza automáticamente cuando se hacen pagos parciales,
 * pero también puede actualizarse manualmente.
 */
@Entity
@Table(name = "workshop_repairs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("WORKSHOP_REPAIR")
public class WorkshopRepairEntity extends AbstractAccountExpenseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "repair_type", nullable = false, length = 50)
    @NotNull(message = "The repair type cannot be null")
    private RepairType repairType; // Tipo de arreglo (obligatorio)

    /**
     * Saldo restante del arreglo.
     * 
     * Se inicializa con amount al crear el WorkshopRepair.
     * Se actualiza automáticamente cuando se crean/eliminan SettlementAllocation,
     * pero también puede actualizarse manualmente para ajustes.
     */
    @Column(name = "remaining_balance", nullable = false)
    @NotNull(message = "The remaining balance cannot be null")
    @Min(value = 0, message = "The remaining balance cannot be negative")
    private Double remainingBalance; // Saldo restante (obligatorio)

    @OneToMany(mappedBy = "accountMovement", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SettlementAllocationEntity> settlementAllocations = new ArrayList<>();
}

