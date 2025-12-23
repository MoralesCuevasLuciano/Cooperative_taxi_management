package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.models.dto.advance.AdvanceCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.advance.AdvanceDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.AdvanceEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.MemberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.PayrollSettlementEntity;
import com.pepotec.cooperative_taxi_managment.repositories.AdvanceRepository;
import com.pepotec.cooperative_taxi_managment.repositories.PayrollSettlementRepository;
import com.pepotec.cooperative_taxi_managment.validators.AdvanceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdvanceService {

    @Autowired
    private AdvanceRepository advanceRepository;

    @Autowired
    private PayrollSettlementRepository payrollSettlementRepository;

    @Autowired
    private MemberAccountService memberAccountService;

    @Autowired
    private AdvanceValidator advanceValidator;

    @Transactional
    public AdvanceDTO create(AdvanceCreateDTO dto) {
        MemberAccountEntity account = memberAccountService.getMemberAccountEntityById(dto.getMemberAccountId());
        advanceValidator.validateMemberRole(account);
        advanceValidator.validateCreateFields(dto.getAmount(), dto.getDate());

        PayrollSettlementEntity settlement = null;
        if (dto.getPayrollSettlementId() != null) {
            settlement = payrollSettlementRepository.findById(dto.getPayrollSettlementId())
                    .orElseThrow(() -> new ResourceNotFoundException(dto.getPayrollSettlementId(), "Payroll Settlement"));
        }

        AdvanceEntity entity = AdvanceEntity.builder()
                .memberAccount(account)
                .payrollSettlement(settlement)
                .movementId(null)
                .date(dto.getDate())
                .amount(dto.getAmount())
                .notes(dto.getNotes())
                .active(true)
                .build();

        return convertToDTO(advanceRepository.save(entity));
    }

    /**
     * Creación desde un movimiento de tipo ADVANCE.
     */
    @Transactional
    public AdvanceEntity createFromMovement(MemberAccountEntity account, LocalDate date, Double amount, Long movementId) {
        advanceValidator.validateMemberRole(account);
        advanceValidator.validateCreateFields(amount, date);
        AdvanceEntity entity = AdvanceEntity.builder()
                .memberAccount(account)
                .payrollSettlement(null)
                .movementId(movementId)
                .date(date)
                .amount(amount)
                .active(true)
                .build();
        return advanceRepository.save(entity);
    }

    @Transactional
    public void deleteByMovementId(Long movementId) {
        advanceRepository.deleteByMovementId(movementId);
    }

    public AdvanceDTO getById(Long id) {
        return convertToDTO(findEntityById(id));
    }

    public List<AdvanceDTO> listAll() {
        return advanceRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<AdvanceDTO> listByAccount(Long memberAccountId) {
        return advanceRepository.findByMemberAccountId(memberAccountId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<AdvanceDTO> listByDateRange(LocalDate startDate, LocalDate endDate) {
        return advanceRepository.findByDateBetween(startDate, endDate).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private AdvanceEntity findEntityById(Long id) {
        return advanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Advance"));
    }

    private AdvanceDTO convertToDTO(AdvanceEntity entity) {
        if (entity == null) return null;
        return AdvanceDTO.builder()
                .id(entity.getId())
                .memberAccountId(entity.getMemberAccount().getId())
                .payrollSettlementId(entity.getPayrollSettlement() != null ? entity.getPayrollSettlement().getId() : null)
                .movementId(entity.getMovementId())
                .date(entity.getDate())
                .amount(entity.getAmount())
                .notes(entity.getNotes())
                .active(entity.getActive())
                .build();
    }
}


