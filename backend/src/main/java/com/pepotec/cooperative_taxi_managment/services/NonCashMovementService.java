package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.models.dto.movement.noncash.NonCashMovementCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.movement.noncash.NonCashMovementDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.person.member.account.MemberAccountDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.person.subscriber.account.SubscriberAccountDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.account.VehicleAccountDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.MemberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.NonCashMovementEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.SubscriberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.VehicleAccountEntity;
import com.pepotec.cooperative_taxi_managment.repositories.NonCashMovementRepository;
import com.pepotec.cooperative_taxi_managment.validators.MovementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NonCashMovementService {

    @Autowired
    private NonCashMovementRepository nonCashMovementRepository;

    @Autowired
    private MovementValidator movementValidator;

    @Autowired
    private BalanceUpdateService balanceUpdateService;

    @Autowired
    private MemberAccountService memberAccountService;

    @Autowired
    private SubscriberAccountService subscriberAccountService;

    @Autowired
    private VehicleAccountService vehicleAccountService;

    @Transactional
    public NonCashMovementDTO create(NonCashMovementCreateDTO dto) {
        movementValidator.validateNonCashMovementCreate(dto);
        NonCashMovementEntity entity = convertCreateDtoToEntity(dto);
        balanceUpdateService.applyMovement(entity);
        return convertToDTO(nonCashMovementRepository.save(entity));
    }

    public NonCashMovementDTO getById(Long id) {
        return convertToDTO(findEntityById(id));
    }

    public List<NonCashMovementDTO> listAll() {
        return nonCashMovementRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<NonCashMovementDTO> listActive() {
        return nonCashMovementRepository.findByActive(true).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<NonCashMovementDTO> listByDateRange(LocalDate startDate, LocalDate endDate) {
        return nonCashMovementRepository.findByDateBetween(startDate, endDate).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<NonCashMovementDTO> listByAccount(Long memberAccountId, Long subscriberAccountId, Long vehicleAccountId) {
        if (memberAccountId != null) {
            return nonCashMovementRepository.findByMemberAccountId(memberAccountId).stream().map(this::convertToDTO).collect(Collectors.toList());
        }
        if (subscriberAccountId != null) {
            return nonCashMovementRepository.findBySubscriberAccountId(subscriberAccountId).stream().map(this::convertToDTO).collect(Collectors.toList());
        }
        if (vehicleAccountId != null) {
            return nonCashMovementRepository.findByVehicleAccountId(vehicleAccountId).stream().map(this::convertToDTO).collect(Collectors.toList());
        }
        return List.of();
    }

    @Transactional
    public NonCashMovementDTO update(Long id, NonCashMovementCreateDTO dto) {
        NonCashMovementEntity existing = findEntityById(id);

        balanceUpdateService.revertMovement(existing);
        movementValidator.validateNonCashMovementCreate(dto);

        NonCashMovementEntity updated = applyDtoToEntity(existing, dto);
        balanceUpdateService.applyMovement(updated);

        return convertToDTO(nonCashMovementRepository.save(updated));
    }

    @Transactional
    public void delete(Long id) {
        NonCashMovementEntity existing = findEntityById(id);
        balanceUpdateService.revertMovement(existing);
        existing.setActive(false);
        nonCashMovementRepository.save(existing);
    }

    private NonCashMovementEntity findEntityById(Long id) {
        return nonCashMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Movimiento no efectivo"));
    }

    private NonCashMovementEntity convertCreateDtoToEntity(NonCashMovementCreateDTO dto) {
        NonCashMovementEntity entity = NonCashMovementEntity.builder()
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .movementType(dto.getMovementType())
                .isIncome(dto.getIsIncome())
                .active(true)
                .build();

        setAccounts(entity, dto.getMemberAccountId(), dto.getSubscriberAccountId(), dto.getVehicleAccountId());
        return entity;
    }

    private NonCashMovementEntity applyDtoToEntity(NonCashMovementEntity entity, NonCashMovementCreateDTO dto) {
        entity.setDescription(dto.getDescription());
        entity.setAmount(dto.getAmount());
        entity.setDate(dto.getDate());
        entity.setMovementType(dto.getMovementType());
        entity.setIsIncome(dto.getIsIncome());
        entity.setActive(true);
        setAccounts(entity, dto.getMemberAccountId(), dto.getSubscriberAccountId(), dto.getVehicleAccountId());
        return entity;
    }

    private void setAccounts(NonCashMovementEntity entity, Long memberAccountId, Long subscriberAccountId, Long vehicleAccountId) {
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

    private NonCashMovementDTO convertToDTO(NonCashMovementEntity entity) {
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

        return NonCashMovementDTO.builder()
                .id(entity.getId())
                .memberAccount(memberAccount)
                .subscriberAccount(subscriberAccount)
                .vehicleAccount(vehicleAccount)
                .description(entity.getDescription())
                .amount(entity.getAmount())
                .date(entity.getDate())
                .movementType(entity.getMovementType())
                .isIncome(entity.getIsIncome())
                .active(entity.getActive())
                .build();
    }
}

