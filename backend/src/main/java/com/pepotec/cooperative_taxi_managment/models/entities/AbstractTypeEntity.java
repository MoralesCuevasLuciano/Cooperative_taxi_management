package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Clase base abstracta para tipos de ingresos y gastos.
 * 
 * Utiliza estrategia JOINED para la herencia, donde las clases hijas
 * (IncomeTypeEntity y ExpenseTypeEntity) tienen sus propias tablas.
 * Esto evita duplicación de columnas comunes en la base de datos.
 */
@Entity
@Table(name = "types")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type_category", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    @NotBlank(message = "The name cannot be empty")
    @Size(min = 3, max = 100, message = "The name must be between 3 and 100 characters")
    private String name; // Nombre del tipo (único)

    @Column(name = "monthly_recurrence", nullable = false)
    @Builder.Default
    private Boolean monthlyRecurrence = false; // Si es recurrente mensualmente

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true; // Soft delete
}

