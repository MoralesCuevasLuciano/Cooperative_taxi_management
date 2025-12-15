package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Clase base para las cuentas de socio, abonado y vehículo.
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_account", unique = true, nullable = false)
    private Long id;

    @Column(name = "balance", nullable = false)
    @NotNull(message = "The balance cannot be null")
    private Double balance;

    @Column(name = "last_modified")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate lastModified;

    @Column(name = "active", nullable = false)
    @NotNull(message = "The active status cannot be null")
    private Boolean active = true;
}


