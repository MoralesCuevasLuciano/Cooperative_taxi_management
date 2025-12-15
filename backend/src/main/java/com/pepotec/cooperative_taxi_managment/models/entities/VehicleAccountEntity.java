package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Cuenta asociada a un vehículo.
 */
@Entity
@Table(name = "vehicle_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class VehicleAccountEntity extends AbstractAccountEntity {

    @OneToOne
    @JoinColumn(name = "id_vehicle", nullable = false, unique = true)
    @NotNull(message = "The vehicle cannot be null")
    private VehicleEntity vehicle;
}


