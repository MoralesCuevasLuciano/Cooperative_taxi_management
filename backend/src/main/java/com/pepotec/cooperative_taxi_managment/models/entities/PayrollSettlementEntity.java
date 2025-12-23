package com.pepotec.cooperative_taxi_managment.models.entities;

import com.pepotec.cooperative_taxi_managment.converters.YearMonthAttributeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payroll_settlements", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_member_account", "year_month"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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

    @Convert(converter = YearMonthAttributeConverter.class)
    @Column(name = "year_month", nullable = false, length = 7)
    @NotNull(message = "The period (yearMonth) cannot be null")
    private YearMonth yearMonth;

    @Column(name = "payment_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate paymentDate; // null => no pagado

    @OneToMany(mappedBy = "payrollSettlement", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<AdvanceEntity> advances = new ArrayList<>();

    @Column(name = "active", nullable = false)
    @NotNull(message = "The active status cannot be null")
    private Boolean active = true;
}


