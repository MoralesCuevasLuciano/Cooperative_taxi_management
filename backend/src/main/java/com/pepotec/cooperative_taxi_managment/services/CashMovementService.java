package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.models.dto.cashregister.CashRegisterDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.movement.cash.CashMovementCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.movement.cash.CashMovementDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.person.member.account.MemberAccountDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.person.subscriber.account.SubscriberAccountDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.account.VehicleAccountDTO;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.models.entities.CashMovementEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.CashRegisterEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.MemberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.SubscriberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.VehicleAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.enums.MovementType;
import com.pepotec.cooperative_taxi_managment.repositories.CashMovementRepository;
import com.pepotec.cooperative_taxi_managment.validators.MovementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CashMovementService {

    @Autowired
    private CashMovementRepository cashMovementRepository;

    @Autowired
    private MovementValidator movementValidator;

    @Autowired
    private BalanceUpdateService balanceUpdateService;

    @Autowired
    private CashRegisterService cashRegisterService;

    @Autowired
    private MemberAccountService memberAccountService;

    @Autowired
    private SubscriberAccountService subscriberAccountService;

    @Autowired
    private VehicleAccountService vehicleAccountService;

    @Autowired
    private AdvanceService advanceService;

    @Transactional
    public CashMovementDTO create(CashMovementCreateDTO dto) {
        movementValidator.validateCashMovementCreate(dto);
        CashMovementEntity entity = convertCreateDtoToEntity(dto);
        balanceUpdateService.applyMovement(entity);
        CashMovementEntity saved = cashMovementRepository.save(entity);

        // Si es ADVANCE, crear Advance asociado
        if (saved.getMovementType() == MovementType.ADVANCE) {
            MemberAccountEntity account = saved.getMemberAccount();
            if (account == null) {
                throw new InvalidDataException("ADVANCE movement requires a MemberAccount");
            }
            advanceService.createFromMovement(account, saved.getDate(), saved.getAmount(), saved.getId());
        }

        return convertToDTO(saved);
    }

    public CashMovementDTO getById(Long id) {
        return convertToDTO(findEntityById(id));
    }

    public List<CashMovementDTO> listAll() {
        return cashMovementRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<CashMovementDTO> listActive() {
        return cashMovementRepository.findByActive(true).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<CashMovementDTO> listByDateRange(LocalDate startDate, LocalDate endDate) {
        return cashMovementRepository.findByDateBetween(startDate, endDate).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<CashMovementDTO> listByAccount(Long memberAccountId, Long subscriberAccountId, Long vehicleAccountId) {
        if (memberAccountId != null) {
            return cashMovementRepository.findByMemberAccountId(memberAccountId).stream().map(this::convertToDTO).collect(Collectors.toList());
        }
        if (subscriberAccountId != null) {
            return cashMovementRepository.findBySubscriberAccountId(subscriberAccountId).stream().map(this::convertToDTO).collect(Collectors.toList());
        }
        if (vehicleAccountId != null) {
            return cashMovementRepository.findByVehicleAccountId(vehicleAccountId).stream().map(this::convertToDTO).collect(Collectors.toList());
        }
        return List.of();
    }

    @Transactional
    public CashMovementDTO update(Long id, CashMovementCreateDTO dto) {
        CashMovementEntity existing = findEntityById(id);

        // Si el previo era ADVANCE, borrar el Advance asociado
        if (existing.getMovementType() == MovementType.ADVANCE) {
            advanceService.deleteByMovementId(existing.getId());
        }

        balanceUpdateService.revertMovement(existing);
        movementValidator.validateCashMovementCreate(dto);

        CashMovementEntity updated = applyDtoToEntity(existing, dto);
        balanceUpdateService.applyMovement(updated);

        CashMovementEntity saved = cashMovementRepository.save(updated);

        if (saved.getMovementType() == MovementType.ADVANCE) {
            MemberAccountEntity account = saved.getMemberAccount();
            if (account == null) {
                throw new InvalidDataException("ADVANCE movement requires a MemberAccount");
            }
            advanceService.createFromMovement(account, saved.getDate(), saved.getAmount(), saved.getId());
        }

        return convertToDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        CashMovementEntity existing = findEntityById(id);

        if (existing.getMovementType() == MovementType.ADVANCE) {
            advanceService.deleteByMovementId(existing.getId());
        }

        balanceUpdateService.revertMovement(existing);
        existing.setActive(false);
        cashMovementRepository.save(existing);
    }

    private CashMovementEntity findEntityById(Long id) {
        return cashMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Movimiento de caja"));
    }

    private CashMovementEntity convertCreateDtoToEntity(CashMovementCreateDTO dto) {
        CashMovementEntity entity = CashMovementEntity.builder()
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .movementType(dto.getMovementType())
                .isIncome(dto.getIsIncome())
                .active(true)
                .cashRegister(cashRegisterService.getOrCreate())
                .build();

        setAccounts(entity, dto.getMemberAccountId(), dto.getSubscriberAccountId(), dto.getVehicleAccountId());
        return entity;
    }

    private CashMovementEntity applyDtoToEntity(CashMovementEntity entity, CashMovementCreateDTO dto) {
        entity.setDescription(dto.getDescription());
        entity.setAmount(dto.getAmount());
        entity.setDate(dto.getDate());
        entity.setMovementType(dto.getMovementType());
        entity.setIsIncome(dto.getIsIncome());
        entity.setActive(true);
        entity.setCashRegister(cashRegisterService.getOrCreate());
        setAccounts(entity, dto.getMemberAccountId(), dto.getSubscriberAccountId(), dto.getVehicleAccountId());
        return entity;
    }

    private void setAccounts(CashMovementEntity entity, Long memberAccountId, Long subscriberAccountId, Long vehicleAccountId) {
        MemberAccountEntity memberAccount = memberAccountId != null
                ? memberAccountService.getMemberAccountEntityById(memberAccountId)
                : null;

        SubscriberAccountEntity subscriberAccount = subscriberAccountId != null
                ? subscriberAccountService.getSubscriberAccountEntityById(subscriberAccountId)
                : null;

        VehicleAccountEntity vehicleAccount = vehicleAccountId != null
                ? vehicleAccountService.getVehicleAccountEntityById(vehicleAccountId)
                : null;

        entity.setMemberAccount(memberAccount);
        entity.setSubscriberAccount(subscriberAccount);
        entity.setVehicleAccount(vehicleAccount);
    }

    private CashMovementDTO convertToDTO(CashMovementEntity entity) {
        if (entity == null) return null;

        MemberAccountDTO memberAccount = null;
        SubscriberAccountDTO subscriberAccount = null;
        VehicleAccountDTO vehicleAccount = null;

        if (entity.getMemberAccount() != null) {
            memberAccount = MemberAccountDTO.builder()
                    .id(entity.getMemberAccount().getId())
                    .memberId(entity.getMemberAccount().getMember().getId())
                    .balance(entity.getMemberAccount().getBalance())
                    .lastModified(entity.getMemberAccount().getLastModified())
                    .active(entity.getMemberAccount().getActive())
                    .build();
        }
        if (entity.getSubscriberAccount() != null) {
            subscriberAccount = SubscriberAccountDTO.builder()
                    .id(entity.getSubscriberAccount().getId())
                    .subscriberId(entity.getSubscriberAccount().getSubscriber().getId())
                    .balance(entity.getSubscriberAccount().getBalance())
                    .lastModified(entity.getSubscriberAccount().getLastModified())
                    .active(entity.getSubscriberAccount().getActive())
                    .build();
        }
        if (entity.getVehicleAccount() != null) {
            vehicleAccount = VehicleAccountDTO.builder()
                    .id(entity.getVehicleAccount().getId())
                    .vehicleId(entity.getVehicleAccount().getVehicle().getId())
                    .balance(entity.getVehicleAccount().getBalance())
                    .lastModified(entity.getVehicleAccount().getLastModified())
                    .active(entity.getVehicleAccount().getActive())
                    .build();
        }

        CashRegisterEntity cashRegister = entity.getCashRegister();
        CashRegisterDTO cashRegisterDTO = CashRegisterDTO.builder()
                .id(cashRegister.getId())
                .amount(cashRegister.getAmount())
                .active(cashRegister.getActive())
                .build();

        return CashMovementDTO.builder()
                .id(entity.getId())
                .memberAccount(memberAccount)
                .subscriberAccount(subscriberAccount)
                .vehicleAccount(vehicleAccount)
                .cashRegister(cashRegisterDTO)
                .description(entity.getDescription())
                .amount(entity.getAmount())
                .date(entity.getDate())
                .movementType(entity.getMovementType())
                .isIncome(entity.getIsIncome())
                .active(entity.getActive())
                .build();
    }
}

