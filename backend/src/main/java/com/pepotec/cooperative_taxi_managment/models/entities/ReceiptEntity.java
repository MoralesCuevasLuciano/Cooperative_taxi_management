package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.pepotec.cooperative_taxi_managment.models.enums.ReceiptType;
import java.time.LocalDate;

/**
 * Entidad que representa un recibo físico otorgado a un socio o abonado
 * cuando pagan una cuota mensual a la cooperativa.
 * 
 * El recibo tiene un número de recibo (que aparece en el papel físico) y
 * un número de talonario (donde está el recibo físico para poder buscarlo).
 */
@Entity
@Table(name = "receipts", uniqueConstraints = {
    // Unicidad: cuenta + period (un recibo por cuenta por período)
    @UniqueConstraint(columnNames = {"id_member_account", "period"}, 
                     name = "uk_member_receipt_period"),
    @UniqueConstraint(columnNames = {"id_subscriber_account", "period"}, 
                     name = "uk_subscriber_receipt_period"),
    // Unicidad: receiptNumber + bookletNumber + receiptType (permite mismo número en diferentes tipos)
    @UniqueConstraint(columnNames = {"receipt_number", "booklet_number", "receipt_type"}, 
                     name = "uk_receipt_booklet_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receipt", unique = true, nullable = false)
    private Long id;

    // Relación con MemberAccount (nullable, solo uno de los dos puede estar presente)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_member_account", nullable = true)
    private MemberAccountEntity memberAccount;

    // Relación con SubscriberAccount (nullable, solo uno de los dos puede estar presente)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subscriber_account", nullable = true)
    private SubscriberAccountEntity subscriberAccount;

    @Column(name = "receipt_number", nullable = false)
    @NotNull(message = "The receipt number cannot be null")
    @Positive(message = "The receipt number must be positive")
    private Integer receiptNumber; // numeroRecibo - número que aparece en el papel físico

    @Column(name = "booklet_number", nullable = false)
    @NotNull(message = "The booklet number cannot be null")
    @Positive(message = "The booklet number must be positive")
    private Integer bookletNumber; // numeroTalonario - número del talonario donde está el recibo

    @Enumerated(EnumType.STRING)
    @Column(name = "receipt_type", nullable = false, length = 20)
    @NotNull(message = "The receipt type cannot be null")
    private ReceiptType receiptType; // tipoRecibo

    /**
     * Campo que almacena el período (año-mes) del recibo en formato "YYYY-MM".
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
    private String yearMonth; // añoMes - período del recibo

    @Column(name = "issue_date", nullable = false)
    @NotNull(message = "The issue date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate issueDate; // fecha de emisión del recibo

    @Column(name = "active", nullable = false)
    @NotNull(message = "The active status cannot be null")
    @Builder.Default
    private Boolean active = true;
}

