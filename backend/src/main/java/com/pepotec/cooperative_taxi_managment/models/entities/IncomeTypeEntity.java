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
 * Entidad que representa un tipo de ingreso.
 * 
 * Los tipos de ingreso pueden ser recurrentes mensualmente (monthlyRecurrence = true),
 * en cuyo caso el sistema creará automáticamente nuevos movimientos mes a mes.
 * 
 * Extiende AbstractTypeEntity para compartir campos comunes con ExpenseTypeEntity.
 */
@Entity
@Table(name = "income_types", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"}, name = "uk_income_type_name")
})
@DiscriminatorValue("INCOME")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class IncomeTypeEntity extends AbstractTypeEntity {

    @OneToMany(mappedBy = "incomeType", fetch = FetchType.LAZY)
    @Builder.Default
    private List<AccountIncomeEntity> accountIncomes = new ArrayList<>();
}

