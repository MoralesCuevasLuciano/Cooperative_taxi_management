package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * NOTA IMPORTANTE SOBRE EL NOMBRE DE LA COLUMNA "period":
 * 
 * La columna se llama "period" en lugar de "year_month" debido a un bug conocido en Hibernate 6
 * al generar DDL para constraints únicos compuestos. Cuando se usaba "year_month", Hibernate
 * generaba SQL inválido con error de sintaxis cerca de esa columna.
 * 
 * El problema NO es el guion bajo en sí (MySQL acepta guiones bajos sin problemas), sino cómo
 * Hibernate 6 procesa ese nombre específico dentro de un @UniqueConstraint compuesto.
 * 
 * Al cambiar a "period", el problema se resolvió completamente. Si en el futuro se necesita
 * cambiar el nombre de esta columna, evitar nombres con guiones bajos cuando estén involucrados
 * en constraints únicos compuestos con Hibernate 6.
 * 
 * Bug reportado: Diciembre 2024 - Hibernate 6.6.29.Final
 * Solución: Cambiar nombre de columna de "year_month" a "period"
 */
@Entity
@Table(name = "payroll_settlements", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_member_account", "period"}, name = "uk_member_account_period")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollSettlementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payroll_settlement", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_member_account", nullable = false)
    @NotNull(message = "The member account cannot be null")
    private MemberAccountEntity memberAccount;

    @Column(name = "gross_salary", nullable = false)
    @NotNull(message = "The gross salary cannot be null")
    private Double grossSalary;

    @Column(name = "net_salary", nullable = false)
    @NotNull(message = "The net salary cannot be null")
    private Double netSalary;

    /**
     * Campo que almacena el período (año-mes) de la liquidación en formato "YYYY-MM".
     * 
     * NOTA: Se usa String en lugar de YearMonth debido a problemas con el AttributeConverter
     * de Hibernate 6 en la generación automática de DDL. La conversión se maneja manualmente
     * en el servicio (PayrollSettlementService).
     * 
     * IMPORTANTE: La columna se llama "period" (no "year_month") debido a un bug de Hibernate 6
     * con constraints únicos compuestos. Ver comentario en la clase para más detalles.
     */
    @Column(name = "period", nullable = false, length = 7)
    @NotNull(message = "The period (yearMonth) cannot be null")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "The period must be in format YYYY-MM (e.g., 2024-12)")
    private String yearMonth;

    @Column(name = "payment_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate paymentDate; // null => no pagado

    @Column(name = "active", nullable = false)
    @NotNull(message = "The active status cannot be null")
    @Builder.Default
    private Boolean active = true;

    @OneToMany(mappedBy = "payrollSettlement", fetch = FetchType.LAZY)
    @Builder.Default
    private List<AdvanceEntity> advances = new ArrayList<>();
}


