package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.models.dto.payrollsettlement.PayrollSettlementCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.payrollsettlement.PayrollSettlementDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.*;
import com.pepotec.cooperative_taxi_managment.repositories.PayrollSettlementRepository;
import com.pepotec.cooperative_taxi_managment.validators.PayrollSettlementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayrollSettlementService {

    @Autowired
    private PayrollSettlementRepository payrollSettlementRepository;

    @Autowired
    private AdvanceService advanceService;

    @Autowired
    private MemberAccountService memberAccountService;

    @Autowired
    private NonCashMovementService nonCashMovementService;

    @Autowired
    private PayrollSettlementValidator payrollSettlementValidator;

    @Transactional
    public PayrollSettlementDTO create(PayrollSettlementCreateDTO dto) {
        MemberAccountEntity account = memberAccountService.getMemberAccountEntityById(dto.getMemberAccountId());
        payrollSettlementValidator.validateMemberRole(account);
        payrollSettlementValidator.validateCreateFields(dto.getGrossSalary(), dto.getYearMonth());
        payrollSettlementValidator.validatePaymentDate(dto.getPaymentDate());

        // Unicidad account + yearMonth
        String yearMonthStr = dto.getYearMonth() != null ? dto.getYearMonth().toString() : null;
        payrollSettlementValidator.validateUniqueAccountPeriod(account.getId(), yearMonthStr, null);

        // Obtener vales (solo los que no tengan liquidación)
        List<AdvanceEntity> advances = new ArrayList<>();
        if (dto.getAdvanceIds() != null && !dto.getAdvanceIds().isEmpty()) {
            advances = advanceService.getAdvanceEntitiesByIds(dto.getAdvanceIds());
            payrollSettlementValidator.validateAdvancesNotLinked(advances, null);
        }

        // Calcular sueldo neto: grossSalary - suma de vales asociados
        Double totalAdvances = advances.stream()
                .mapToDouble(AdvanceEntity::getAmount)
                .sum();
        Double netSalary = dto.getGrossSalary() - totalAdvances;
        if (netSalary < 0) {
            netSalary = 0.0; // El sueldo neto no puede ser negativo
        }

        PayrollSettlementEntity entity = PayrollSettlementEntity.builder()
                .memberAccount(account)
                .grossSalary(dto.getGrossSalary())
                .netSalary(netSalary) // Calculado automáticamente
                .yearMonth(dto.getYearMonth() != null ? dto.getYearMonth().toString() : null) // Convertir YearMonth a String
                .paymentDate(dto.getPaymentDate())
                .active(true)
                .build();

        // Guardar primero la entidad para que tenga ID
        entity = payrollSettlementRepository.save(entity);

        // Actualizar vales existentes para asociarlos con esta liquidación
        for (AdvanceEntity adv : advances) {
            advanceService.associateWithSettlement(adv, entity);
        }
        entity.setAdvances(advances);

        // Si tiene paymentDate, registrar movimiento de pago (NonCashMovement) por grossSalary
        if (entity.getPaymentDate() != null) {
            nonCashMovementService.createPaymentForSettlement(entity);
        }

        return convertToDTO(entity);
    }

    @Transactional
    public PayrollSettlementDTO update(Long id, PayrollSettlementCreateDTO dto) {
        PayrollSettlementEntity existing = findEntityById(id);

        MemberAccountEntity account = memberAccountService.getMemberAccountEntityById(dto.getMemberAccountId());
        payrollSettlementValidator.validateMemberRole(account);
        payrollSettlementValidator.validateCreateFields(dto.getGrossSalary(), dto.getYearMonth());
        payrollSettlementValidator.validatePaymentDate(dto.getPaymentDate());

        // Unicidad account + yearMonth (permitiendo el mismo id)
        String yearMonthStr = dto.getYearMonth() != null ? dto.getYearMonth().toString() : null;
        payrollSettlementValidator.validateUniqueAccountPeriod(account.getId(), yearMonthStr, id);

        // Limpiar enlaces previos de advances (actualizar para desasociarlos)
        if (existing.getAdvances() != null) {
            for (AdvanceEntity adv : existing.getAdvances()) {
                advanceService.disassociateFromSettlement(adv);
            }
        }
        existing.setAdvances(new ArrayList<>());

        // Asociar nuevos vales (actualizar para asociarlos)
        List<AdvanceEntity> advances = new ArrayList<>();
        if (dto.getAdvanceIds() != null && !dto.getAdvanceIds().isEmpty()) {
            advances = advanceService.getAdvanceEntitiesByIds(dto.getAdvanceIds());
            payrollSettlementValidator.validateAdvancesNotLinked(advances, id);
            for (AdvanceEntity adv : advances) {
                advanceService.associateWithSettlement(adv, existing);
            }
        }

        // Calcular sueldo neto: grossSalary - suma de vales asociados
        Double totalAdvances = advances.stream()
                .mapToDouble(AdvanceEntity::getAmount)
                .sum();
        Double netSalary = dto.getGrossSalary() - totalAdvances;
        if (netSalary < 0) {
            netSalary = 0.0; // El sueldo neto no puede ser negativo
        }

        existing.setMemberAccount(account);
        existing.setGrossSalary(dto.getGrossSalary());
        existing.setNetSalary(netSalary); // Calculado automáticamente
        existing.setYearMonth(dto.getYearMonth() != null ? dto.getYearMonth().toString() : null); // Convertir YearMonth a String
        existing.setPaymentDate(dto.getPaymentDate());
        existing.setAdvances(advances);

        existing = payrollSettlementRepository.save(existing);

        // Si ahora tiene paymentDate y antes era null, registrar movimiento de pago
        if (existing.getPaymentDate() != null) {
            nonCashMovementService.createPaymentForSettlement(existing);
        }

        return convertToDTO(existing);
    }

    public PayrollSettlementDTO getById(Long id) {
        return convertToDTO(findEntityById(id));
    }

    public List<PayrollSettlementDTO> listAll() {
        return payrollSettlementRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<PayrollSettlementDTO> listByAccount(Long memberAccountId) {
        return payrollSettlementRepository.findByMemberAccountId(memberAccountId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<PayrollSettlementDTO> listByPeriod(java.time.YearMonth yearMonth) {
        String yearMonthStr = yearMonth != null ? yearMonth.toString() : null;
        return payrollSettlementRepository.findByYearMonth(yearMonthStr).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<PayrollSettlementDTO> listByPaymentDateRange(LocalDate startDate, LocalDate endDate) {
        return payrollSettlementRepository.findByPaymentDateBetween(startDate, endDate).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        PayrollSettlementEntity existing = findEntityById(id);
        existing.setActive(false);
        payrollSettlementRepository.save(existing);
    }

    private PayrollSettlementEntity findEntityById(Long id) {
        return payrollSettlementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Payroll Settlement"));
    }

    private PayrollSettlementDTO convertToDTO(PayrollSettlementEntity entity) {
        if (entity == null) return null;
        return PayrollSettlementDTO.builder()
                .id(entity.getId())
                .memberAccountId(entity.getMemberAccount().getId())
                .grossSalary(entity.getGrossSalary())
                .netSalary(entity.getNetSalary())
                .yearMonth(entity.getYearMonth() != null ? YearMonth.parse(entity.getYearMonth()) : null) // Convertir String a YearMonth
                .paymentDate(entity.getPaymentDate())
                .active(entity.getActive())
                .advances(entity.getAdvances() != null
                        ? entity.getAdvances().stream().map(adv ->
                        com.pepotec.cooperative_taxi_managment.models.dto.advance.AdvanceDTO.builder()
                                .id(adv.getId())
                                .memberAccountId(adv.getMemberAccount().getId())
                                .payrollSettlementId(adv.getPayrollSettlement() != null ? adv.getPayrollSettlement().getId() : null)
                                .movementId(adv.getMovementId())
                                .date(adv.getDate())
                                .amount(adv.getAmount())
                                .notes(adv.getNotes())
                                .active(adv.getActive())
                                .build()
                ).collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }
}


