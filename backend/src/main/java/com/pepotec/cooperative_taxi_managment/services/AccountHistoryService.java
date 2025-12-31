package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.models.dto.accounthistory.AccountHistoryCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.accounthistory.AccountHistoryDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.AccountHistoryEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.MemberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.SubscriberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.VehicleAccountEntity;
import com.pepotec.cooperative_taxi_managment.repositories.AccountHistoryRepository;
import com.pepotec.cooperative_taxi_managment.validators.AccountHistoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountHistoryService {

    @Autowired
    private AccountHistoryRepository accountHistoryRepository;

    @Autowired
    private MemberAccountService memberAccountService;

    @Autowired
    private SubscriberAccountService subscriberAccountService;

    @Autowired
    private VehicleAccountService vehicleAccountService;

    @Autowired
    private AccountHistoryValidator accountHistoryValidator;

    @Transactional
    public AccountHistoryDTO create(AccountHistoryCreateDTO dto) {
        accountHistoryValidator.validateCreateFields(dto);

        // Validar unicidad: cuenta + period
        String periodStr = dto.getYearMonth() != null ? dto.getYearMonth().toString() : null;
        accountHistoryValidator.validateUniqueAccountPeriod(
                dto.getMemberAccountId(), 
                dto.getSubscriberAccountId(), 
                dto.getVehicleAccountId(), 
                periodStr, 
                null
        );

        // Obtener cuenta según el tipo
        MemberAccountEntity memberAccount = null;
        SubscriberAccountEntity subscriberAccount = null;
        VehicleAccountEntity vehicleAccount = null;
        
        if (dto.getMemberAccountId() != null) {
            memberAccount = memberAccountService.getMemberAccountEntityById(dto.getMemberAccountId());
        } else if (dto.getSubscriberAccountId() != null) {
            subscriberAccount = subscriberAccountService.getSubscriberAccountEntityById(dto.getSubscriberAccountId());
        } else if (dto.getVehicleAccountId() != null) {
            vehicleAccount = vehicleAccountService.getVehicleAccountEntityById(dto.getVehicleAccountId());
        }

        AccountHistoryEntity entity = AccountHistoryEntity.builder()
                .memberAccount(memberAccount)
                .subscriberAccount(subscriberAccount)
                .vehicleAccount(vehicleAccount)
                .yearMonth(periodStr) // Convertir YearMonth a String
                .registrationDate(dto.getRegistrationDate())
                .monthEndBalance(dto.getMonthEndBalance())
                .active(true)
                .build();

        accountHistoryValidator.validate(entity);

        return convertToDTO(accountHistoryRepository.save(entity));
    }

    public AccountHistoryDTO getById(Long id) {
        return convertToDTO(findEntityById(id));
    }

    public List<AccountHistoryDTO> listAll() {
        return accountHistoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AccountHistoryDTO> listByMemberAccount(Long memberAccountId) {
        return accountHistoryRepository.findByMemberAccountIdAndActiveTrue(memberAccountId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AccountHistoryDTO> listBySubscriberAccount(Long subscriberAccountId) {
        return accountHistoryRepository.findBySubscriberAccountIdAndActiveTrue(subscriberAccountId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AccountHistoryDTO> listByVehicleAccount(Long vehicleAccountId) {
        return accountHistoryRepository.findByVehicleAccountIdAndActiveTrue(vehicleAccountId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AccountHistoryDTO> listByPeriod(YearMonth yearMonth) {
        String periodStr = yearMonth != null ? yearMonth.toString() : null;
        return accountHistoryRepository.findByPeriod(periodStr).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AccountHistoryDTO> listByRegistrationDateRange(LocalDate startDate, LocalDate endDate) {
        return accountHistoryRepository.findByRegistrationDateBetweenAndActiveTrue(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountHistoryDTO update(Long id, AccountHistoryCreateDTO dto) {
        AccountHistoryEntity existing = findEntityById(id);
        accountHistoryValidator.validateCreateFields(dto);

        // Validar unicidad: cuenta + period (excluyendo el actual)
        String periodStr = dto.getYearMonth() != null ? dto.getYearMonth().toString() : null;
        accountHistoryValidator.validateUniqueAccountPeriod(
                dto.getMemberAccountId(), 
                dto.getSubscriberAccountId(), 
                dto.getVehicleAccountId(), 
                periodStr, 
                id
        );

        // Obtener cuenta según el tipo
        MemberAccountEntity memberAccount = null;
        SubscriberAccountEntity subscriberAccount = null;
        VehicleAccountEntity vehicleAccount = null;
        
        if (dto.getMemberAccountId() != null) {
            memberAccount = memberAccountService.getMemberAccountEntityById(dto.getMemberAccountId());
        } else if (dto.getSubscriberAccountId() != null) {
            subscriberAccount = subscriberAccountService.getSubscriberAccountEntityById(dto.getSubscriberAccountId());
        } else if (dto.getVehicleAccountId() != null) {
            vehicleAccount = vehicleAccountService.getVehicleAccountEntityById(dto.getVehicleAccountId());
        }

        existing.setMemberAccount(memberAccount);
        existing.setSubscriberAccount(subscriberAccount);
        existing.setVehicleAccount(vehicleAccount);
        existing.setYearMonth(periodStr);
        existing.setRegistrationDate(dto.getRegistrationDate());
        existing.setMonthEndBalance(dto.getMonthEndBalance());

        accountHistoryValidator.validate(existing);

        return convertToDTO(accountHistoryRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        AccountHistoryEntity existing = findEntityById(id);
        existing.setActive(false);
        accountHistoryRepository.save(existing);
    }

    private AccountHistoryEntity findEntityById(Long id) {
        return accountHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Account History"));
    }

    private AccountHistoryDTO convertToDTO(AccountHistoryEntity entity) {
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

        return AccountHistoryDTO.builder()
                .id(entity.getId())
                .memberAccountId(entity.getMemberAccount() != null ? entity.getMemberAccount().getId() : null)
                .subscriberAccountId(entity.getSubscriberAccount() != null ? entity.getSubscriberAccount().getId() : null)
                .vehicleAccountId(entity.getVehicleAccount() != null ? entity.getVehicleAccount().getId() : null)
                .yearMonth(yearMonth)
                .registrationDate(entity.getRegistrationDate())
                .monthEndBalance(entity.getMonthEndBalance())
                .active(entity.getActive())
                .build();
    }
}

