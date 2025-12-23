package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa la caja física (singleton).
 * Solo debe existir UNA instancia en todo el sistema.
 * Representa el dinero en efectivo (billetes) disponible en la caja.
 */
@Entity
@Table(name = "cash_register")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashRegisterEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cash_register", unique = true, nullable = false)
    private Long id;
    
    @Column(name = "amount", nullable = false)
    @NotNull(message = "The amount cannot be null")
    // NOTA: Se permite negativo para detectar errores de registro
    private Double amount = 0.0;
    
    @Column(name = "active", nullable = false)
    @NotNull(message = "The active status cannot be null")
    private Boolean active = true;
    
    // NOTA: La lógica de actualización se maneja en el Service, NO en la entidad
}




