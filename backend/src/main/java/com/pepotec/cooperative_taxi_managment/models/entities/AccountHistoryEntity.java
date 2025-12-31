package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Entidad que representa el historial mensual de una cuenta.
 * 
 * Se genera automáticamente al inicio de cada mes (00:00 del día 1) para registrar
 * el saldo de cierre del mes anterior. Por ejemplo, el 01/01/2026 a las 00:00 se
 * genera el AccountHistory para diciembre 2025 con el saldo de cierre de ese mes.
 * 
 * Permite llevar un registro histórico del saldo de cada cuenta mes a mes.
 */
@Entity
@Table(name = "account_histories", uniqueConstraints = {
    // Unicidad: cuenta + period (un historial por cuenta por mes)
    @UniqueConstraint(columnNames = {"id_member_account", "period"}, 
                     name = "uk_member_account_history_period"),
    @UniqueConstraint(columnNames = {"id_subscriber_account", "period"}, 
                     name = "uk_subscriber_account_history_period"),
    @UniqueConstraint(columnNames = {"id_vehicle_account", "period"}, 
                     name = "uk_vehicle_account_history_period")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_account_history", unique = true, nullable = false)
    private Long id;

    // Relación con MemberAccount (nullable, solo una de las tres puede estar presente)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_member_account", nullable = true)
    private MemberAccountEntity memberAccount;

    // Relación con SubscriberAccount (nullable, solo una de las tres puede estar presente)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subscriber_account", nullable = true)
    private SubscriberAccountEntity subscriberAccount;

    // Relación con VehicleAccount (nullable, solo una de las tres puede estar presente)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehicle_account", nullable = true)
    private VehicleAccountEntity vehicleAccount;

    /**
     * Campo que almacena el período (año-mes) del historial en formato "YYYY-MM".
     * Representa el mes al que corresponde el cierre (ej: "2025-12" para diciembre 2025).
     * 
     * NOTA: Se usa String en lugar de YearMonth para evitar problemas con Hibernate 6
     * en la generación automática de DDL. La conversión se maneja manualmente en el servicio.
     * 
     * IMPORTANTE: La columna se llama "period" (no "year_month") debido a un bug conocido
     * de Hibernate 6 con constraints únicos compuestos. Ver PayrollSettlementEntity para más detalles.
     */
    @Column(name = "period", nullable = false, length = 7)
    @NotNull(message = "The period (yearMonth) cannot be null")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "The period must be in format YYYY-MM (e.g., 2024-12)")
    private String yearMonth; // añoMes - mes al que corresponde el cierre

    /**
     * Fecha exacta en que se registró el historial.
     * Se genera automáticamente al inicio de cada mes (00:00 del día 1) para el mes anterior.
     * Ejemplo: Si se genera el 2026-01-01 a las 00:00, el period será "2025-12" (diciembre 2025).
     */
    @Column(name = "registration_date", nullable = false)
    @NotNull(message = "The registration date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate registrationDate; // fecha exacta de registro

    @Column(name = "month_end_balance", nullable = false)
    @NotNull(message = "The month end balance cannot be null")
    private Double monthEndBalance; // saldoCierreMes - saldo al finalizar el mes (puede ser negativo)

    @Column(name = "active", nullable = false)
    @NotNull(message = "The active status cannot be null")
    @Builder.Default
    private Boolean active = true;
}

