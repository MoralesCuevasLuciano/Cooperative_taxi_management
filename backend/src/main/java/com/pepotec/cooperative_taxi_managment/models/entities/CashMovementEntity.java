package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entidad que representa movimientos de dinero en EFECTIVO (con billetes).
 * Extiende AbstractMovementEntity y agrega relación con CashRegister.
 * Afecta TANTO una cuenta (si existe) COMO la caja física.
 */
@Entity
@Table(name = "cash_movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CashMovementEntity extends AbstractMovementEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cash_register", nullable = false)
    @NotNull(message = "The cash register cannot be null")
    private CashRegisterEntity cashRegister;
}




