package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * Entidad que representa el historial diario de la caja.
 * Se crea al comenzar el día con el monto inicial.
 * El monto final se actualiza al cerrar el día (puede ser null si el día aún no se cerró).
 */
@Entity
@Table(name = "cash_register_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CashRegisterHistoryEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cash_register_history", unique = true, nullable = false)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cash_register", nullable = false)
    @NotNull(message = "The cash register cannot be null")
    private CashRegisterEntity cashRegister;
    
    @Column(name = "initial_amount", nullable = false)
    @NotNull(message = "The initial amount cannot be null")
    // NOTA: Se permite negativo para detectar errores de registro
    private Double initialAmount;
    
    @Column(name = "final_amount")
    // NOTA: Puede ser null al crearse (se crea al comenzar el día)
    // Se actualiza al finalizar el día o cuando se cierre el historial
    private Double finalAmount;
    
    @Column(name = "date", nullable = false, unique = true)
    @NotNull(message = "The date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
}




