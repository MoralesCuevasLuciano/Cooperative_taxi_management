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
 * Entidad que representa un ingreso de cuenta.
 * 
 * Extiende AbstractAccountMovementEntity y utiliza estrategia JOINED.
 * El amount es positivo y representa un ingreso (suma al saldo cuando se agrega).
 * 
 * Unicidad: (account + yearMonth + incomeType) solo aplica cuando 
 * incomeType.monthlyRecurrence = true
 */
@Entity
@Table(name = "account_incomes", indexes = {
    @Index(name = "idx_member_account_period_added", 
           columnList = "id_member_account, period, added"),
    @Index(name = "idx_subscriber_account_period_added", 
           columnList = "id_subscriber_account, period, added"),
    @Index(name = "idx_vehicle_account_period_added", 
           columnList = "id_vehicle_account, period, added")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("INCOME")
public class AccountIncomeEntity extends AbstractAccountMovementEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_income_type", nullable = true)
    private IncomeTypeEntity incomeType;

    @OneToMany(mappedBy = "accountMovement", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SettlementAllocationEntity> settlementAllocations = new ArrayList<>();
}

