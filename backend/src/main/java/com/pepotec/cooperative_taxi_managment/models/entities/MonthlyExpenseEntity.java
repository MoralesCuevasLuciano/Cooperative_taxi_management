package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un gasto mensual recurrente.
 * 
 * Extiende AbstractAccountExpenseEntity y utiliza estrategia JOINED.
 * El amount es positivo pero representa un egreso (se resta del saldo cuando se agrega).
 * 
 * Unicidad: (account + yearMonth + expenseType) solo aplica cuando 
 * expenseType.monthlyRecurrence = true
 * 
 * Puede tener cuotas (currentInstallment y finalInstallment).
 */
@Entity
@Table(name = "monthly_expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("MONTHLY_EXPENSE")
public class MonthlyExpenseEntity extends AbstractAccountExpenseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_expense_type", nullable = true)
    private ExpenseTypeEntity expenseType;

    @OneToMany(mappedBy = "accountMovement", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SettlementAllocationEntity> settlementAllocations = new ArrayList<>();
}

