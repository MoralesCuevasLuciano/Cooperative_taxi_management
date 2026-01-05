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
 * Entidad que representa un tipo de gasto.
 * 
 * Los tipos de gasto pueden ser recurrentes mensualmente (monthlyRecurrence = true),
 * en cuyo caso el sistema creará automáticamente nuevos movimientos mes a mes.
 * 
 * Extiende AbstractTypeEntity para compartir campos comunes con IncomeTypeEntity.
 */
@Entity
@Table(name = "expense_types", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"}, name = "uk_expense_type_name")
})
@DiscriminatorValue("EXPENSE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ExpenseTypeEntity extends AbstractTypeEntity {

    @OneToMany(mappedBy = "expenseType", fetch = FetchType.LAZY)
    @Builder.Default
    private List<MonthlyExpenseEntity> monthlyExpenses = new ArrayList<>();
}

