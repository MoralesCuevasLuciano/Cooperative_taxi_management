package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Clase abstracta intermedia para agrupar gastos de cuenta.
 * 
 * Extiende AbstractAccountMovementEntity y utiliza estrategia JOINED.
 * El amount es positivo pero representa un egreso (se resta del saldo cuando se agrega).
 * 
 * Esta clase no se instancia directamente, solo sirve como base para
 * MonthlyExpenseEntity y WorkshopRepairEntity.
 */
@Entity
@Table(name = "account_expenses")
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("EXPENSE")
public abstract class AbstractAccountExpenseEntity extends AbstractAccountMovementEntity {
    // Sin atributos adicionales, solo estructura para herencia
}

