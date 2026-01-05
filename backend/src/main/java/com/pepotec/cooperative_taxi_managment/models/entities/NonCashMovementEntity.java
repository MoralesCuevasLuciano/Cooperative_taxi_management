package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entidad que representa movimientos de dinero SIN EFECTIVO (transferencias, débitos, créditos).
 * Extiende AbstractMovementEntity.
 * Solo afecta una cuenta (si existe), NO afecta la caja física.
 */
@Entity
@Table(name = "non_cash_movements")
@DiscriminatorValue("NON_CASH")
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class NonCashMovementEntity extends AbstractMovementEntity {
    // No tiene campos adicionales, solo extiende AbstractMovementEntity
}

