package com.pepotec.cooperative_taxi_managment.models.entities;

import com.pepotec.cooperative_taxi_managment.models.enums.MovementType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * Clase base abstracta para movimientos de dinero (efectivo y no efectivo).
 * Usa @MappedSuperclass para compartir campos comunes entre CashMovement y NonCashMovement.
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractMovementEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movement", unique = true, nullable = false)
    private Long id;
    
    // Relación polimórfica con cuentas (todas pueden ser null, pero solo una puede estar presente)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_member_account")
    private MemberAccountEntity memberAccount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subscriber_account")
    private SubscriberAccountEntity subscriberAccount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehicle_account")
    private VehicleAccountEntity vehicleAccount;
    
    @Column(name = "description", nullable = false)
    @NotBlank(message = "The description cannot be empty")
    @Size(min = 3, max = 255, message = "The description must be between 3 and 255 characters")
    private String description;
    
    @Column(name = "amount", nullable = false)
    @NotNull(message = "The amount cannot be null")
    // NOTA: Se permite negativo para detectar errores de registro
    private Double amount;
    
    @Column(name = "date", nullable = false)
    @NotNull(message = "The date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    @NotNull(message = "The movement type cannot be null")
    private MovementType movementType;
    
    @Column(name = "is_income", nullable = false)
    @NotNull(message = "The isIncome flag cannot be null")
    // true = Ingreso (suma a cuenta/caja), false = Egreso (resta de cuenta/caja)
    private Boolean isIncome;
    
    @Column(name = "active", nullable = false)
    @NotNull(message = "The active status cannot be null")
    private Boolean active = true;
    
    // NOTA: Auditoría se implementará más adelante
    
    /**
     * Método helper para obtener la cuenta afectada.
     * Retorna la primera cuenta no-null encontrada, o null si todas son null.
     */
    public AbstractAccountEntity getAffectedAccount() {
        if (memberAccount != null) return memberAccount;
        if (subscriberAccount != null) return subscriberAccount;
        if (vehicleAccount != null) return vehicleAccount;
        return null;
    }
}

