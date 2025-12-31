package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.models.dto.receipt.ReceiptCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.receipt.ReceiptDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.MemberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.ReceiptEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.SubscriberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.enums.ReceiptType;
import com.pepotec.cooperative_taxi_managment.repositories.ReceiptRepository;
import com.pepotec.cooperative_taxi_managment.validators.ReceiptValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private MemberAccountService memberAccountService;

    @Autowired
    private SubscriberAccountService subscriberAccountService;

    @Autowired
    private ReceiptValidator receiptValidator;

    @Transactional
    public ReceiptDTO create(ReceiptCreateDTO dto) {
        receiptValidator.validateCreateFields(dto);

        // Validar unicidad: cuenta + period
        String periodStr = dto.getYearMonth() != null ? dto.getYearMonth().toString() : null;
        receiptValidator.validateUniqueAccountPeriod(dto.getMemberAccountId(), dto.getSubscriberAccountId(), periodStr, null);

        // Validar unicidad: receiptNumber + bookletNumber + receiptType
        receiptValidator.validateUniqueReceiptBooklet(dto.getReceiptNumber(), dto.getBookletNumber(), dto.getReceiptType(), null);

        // Obtener cuenta según el tipo
        MemberAccountEntity memberAccount = null;
        SubscriberAccountEntity subscriberAccount = null;
        if (dto.getMemberAccountId() != null) {
            memberAccount = memberAccountService.getMemberAccountEntityById(dto.getMemberAccountId());
        } else if (dto.getSubscriberAccountId() != null) {
            subscriberAccount = subscriberAccountService.getSubscriberAccountEntityById(dto.getSubscriberAccountId());
        }

        ReceiptEntity entity = ReceiptEntity.builder()
                .memberAccount(memberAccount)
                .subscriberAccount(subscriberAccount)
                .receiptNumber(dto.getReceiptNumber())
                .bookletNumber(dto.getBookletNumber())
                .receiptType(dto.getReceiptType())
                .yearMonth(periodStr) // Convertir YearMonth a String
                .issueDate(dto.getIssueDate())
                .active(true)
                .build();

        receiptValidator.validate(entity);

        return convertToDTO(receiptRepository.save(entity));
    }

    public ReceiptDTO getById(Long id) {
        return convertToDTO(findEntityById(id));
    }

    public List<ReceiptDTO> listAll() {
        return receiptRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReceiptDTO> listByMemberAccount(Long memberAccountId) {
        return receiptRepository.findByMemberAccountIdAndActiveTrue(memberAccountId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReceiptDTO> listBySubscriberAccount(Long subscriberAccountId) {
        return receiptRepository.findBySubscriberAccountIdAndActiveTrue(subscriberAccountId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReceiptDTO> listByPeriod(YearMonth yearMonth) {
        String periodStr = yearMonth != null ? yearMonth.toString() : null;
        return receiptRepository.findByPeriod(periodStr).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReceiptDTO> listByReceiptType(ReceiptType receiptType) {
        return receiptRepository.findByReceiptType(receiptType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReceiptDTO> listByIssueDateRange(LocalDate startDate, LocalDate endDate) {
        return receiptRepository.findByIssueDateBetweenAndActiveTrue(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReceiptDTO update(Long id, ReceiptCreateDTO dto) {
        ReceiptEntity existing = findEntityById(id);
        receiptValidator.validateCreateFields(dto);

        // Validar unicidad: cuenta + period (excluyendo el actual)
        String periodStr = dto.getYearMonth() != null ? dto.getYearMonth().toString() : null;
        receiptValidator.validateUniqueAccountPeriod(dto.getMemberAccountId(), dto.getSubscriberAccountId(), periodStr, id);

        // Validar unicidad: receiptNumber + bookletNumber + receiptType (excluyendo el actual)
        receiptValidator.validateUniqueReceiptBooklet(dto.getReceiptNumber(), dto.getBookletNumber(), dto.getReceiptType(), id);

        // Obtener cuenta según el tipo
        MemberAccountEntity memberAccount = null;
        SubscriberAccountEntity subscriberAccount = null;
        if (dto.getMemberAccountId() != null) {
            memberAccount = memberAccountService.getMemberAccountEntityById(dto.getMemberAccountId());
        } else if (dto.getSubscriberAccountId() != null) {
            subscriberAccount = subscriberAccountService.getSubscriberAccountEntityById(dto.getSubscriberAccountId());
        }

        existing.setMemberAccount(memberAccount);
        existing.setSubscriberAccount(subscriberAccount);
        existing.setReceiptNumber(dto.getReceiptNumber());
        existing.setBookletNumber(dto.getBookletNumber());
        existing.setReceiptType(dto.getReceiptType());
        existing.setYearMonth(periodStr);
        existing.setIssueDate(dto.getIssueDate());

        receiptValidator.validate(existing);

        return convertToDTO(receiptRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        ReceiptEntity existing = findEntityById(id);
        existing.setActive(false);
        receiptRepository.save(existing);
    }

    private ReceiptEntity findEntityById(Long id) {
        return receiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Receipt"));
    }

    private ReceiptDTO convertToDTO(ReceiptEntity entity) {
        if (entity == null) return null;
        
        // Convertir String a YearMonth
        YearMonth yearMonth = null;
        if (entity.getYearMonth() != null && !entity.getYearMonth().isEmpty()) {
            try {
                yearMonth = YearMonth.parse(entity.getYearMonth());
            } catch (Exception e) {
                // Si falla el parse, yearMonth queda null
            }
        }

        return ReceiptDTO.builder()
                .id(entity.getId())
                .memberAccountId(entity.getMemberAccount() != null ? entity.getMemberAccount().getId() : null)
                .subscriberAccountId(entity.getSubscriberAccount() != null ? entity.getSubscriberAccount().getId() : null)
                .receiptNumber(entity.getReceiptNumber())
                .bookletNumber(entity.getBookletNumber())
                .receiptType(entity.getReceiptType())
                .yearMonth(yearMonth)
                .issueDate(entity.getIssueDate())
                .active(entity.getActive())
                .build();
    }
}

