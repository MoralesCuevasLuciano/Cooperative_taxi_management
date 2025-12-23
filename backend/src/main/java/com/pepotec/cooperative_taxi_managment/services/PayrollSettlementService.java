package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.models.dto.payrollsettlement.PayrollSettlementCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.payrollsettlement.PayrollSettlementDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.*;
import com.pepotec.cooperative_taxi_managment.repositories.AdvanceRepository;
import com.pepotec.cooperative_taxi_managment.repositories.PayrollSettlementRepository;
import com.pepotec.cooperative_taxi_managment.validators.PayrollSettlementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayrollSettlementService {

    @Autowired
    private PayrollSettlementRepository payrollSettlementRepository;

    @Autowired
    private AdvanceRepository advanceRepository;

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
        payrollSettlementValidator.validateCreateFields(dto.getGrossSalary(), dto.getNetSalary(), dto.getYearMonth());
        payrollSettlementValidator.validatePaymentDate(dto.getPaymentDate());

        // Unicidad account + yearMonth
        if (payrollSettlementRepository.existsByMemberAccountIdAndYearMonth(account.getId(), dto.getYearMonth())) {
            throw new InvalidDataException("A payroll settlement already exists for this account and period");
        }

        PayrollSettlementEntity entity = PayrollSettlementEntity.builder()
                .memberAccount(account)
                .grossSalary(dto.getGrossSalary())
                .netSalary(dto.getNetSalary())
                .yearMonth(dto.getYearMonth())
                .paymentDate(dto.getPaymentDate())
                .active(true)
                .build();

        // Asociar vales (solo los que no tengan liquidación)
        if (dto.getAdvanceIds() != null && !dto.getAdvanceIds().isEmpty()) {
            List<AdvanceEntity> advances = advanceRepository.findAllById(dto.getAdvanceIds());
            for (AdvanceEntity adv : advances) {
                if (adv.getPayrollSettlement() != null) {
                    throw new InvalidDataException("Advance " + adv.getId() + " is already linked to a payroll settlement");
                }
                adv.setPayrollSettlement(entity);
            }
            entity.setAdvances(advances);
        } else {
            entity.setAdvances(new ArrayList<>());
        }

        entity = payrollSettlementRepository.save(entity);

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
        payrollSettlementValidator.validateCreateFields(dto.getGrossSalary(), dto.getNetSalary(), dto.getYearMonth());
        payrollSettlementValidator.validatePaymentDate(dto.getPaymentDate());

        // Unicidad account + yearMonth (permitiendo el mismo id)
        payrollSettlementRepository.findByMemberAccountIdAndYearMonth(account.getId(), dto.getYearMonth())
                .ifPresent(other -> {
                    if (!other.getId().equals(id)) {
                        throw new InvalidDataException("A payroll settlement already exists for this account and period");
                    }
                });

        existing.setMemberAccount(account);
        existing.setGrossSalary(dto.getGrossSalary());
        existing.setNetSalary(dto.getNetSalary());
        existing.setYearMonth(dto.getYearMonth());
        existing.setPaymentDate(dto.getPaymentDate());

        // Limpiar enlaces previos de advances y asignar los nuevos
        if (existing.getAdvances() != null) {
            existing.getAdvances().forEach(a -> a.setPayrollSettlement(null));
        }
        existing.setAdvances(new ArrayList<>());

        if (dto.getAdvanceIds() != null && !dto.getAdvanceIds().isEmpty()) {
            List<AdvanceEntity> advances = advanceRepository.findAllById(dto.getAdvanceIds());
            for (AdvanceEntity adv : advances) {
                if (adv.getPayrollSettlement() != null && !adv.getPayrollSettlement().getId().equals(existing.getId())) {
                    throw new InvalidDataException("Advance " + adv.getId() + " is already linked to another payroll settlement");
                }
                adv.setPayrollSettlement(existing);
            }
            existing.setAdvances(advances);
        }

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
        return payrollSettlementRepository.findByYearMonth(yearMonth).stream().map(this::convertToDTO).collect(Collectors.toList());
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
                .yearMonth(entity.getYearMonth())
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


