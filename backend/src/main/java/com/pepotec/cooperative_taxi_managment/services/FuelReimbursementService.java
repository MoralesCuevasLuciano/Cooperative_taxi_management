package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.models.dto.fuelreimbursement.FuelReimbursementDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.fuelreimbursement.FuelReimbursementCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.FuelReimbursementEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.MemberAccountEntity;
import com.pepotec.cooperative_taxi_managment.repositories.FuelReimbursementRepository;
import com.pepotec.cooperative_taxi_managment.validators.FuelReimbursementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuelReimbursementService {

    @Autowired
    private FuelReimbursementRepository fuelReimbursementRepository;

    @Autowired
    private MemberAccountService memberAccountService;

    @Autowired
    private FuelReimbursementValidator fuelReimbursementValidator;

    /**
     * Crea un nuevo registro de reintegro de combustible para una cuenta de socio.
     * Si ya existe uno activo, lanza excepción.
     */
    @Transactional
    public FuelReimbursementDTO createFuelReimbursement(Long memberAccountId, FuelReimbursementCreateDTO fuelReimbursement) {
        fuelReimbursementValidator.validateFuelReimbursementCreateFields(fuelReimbursement);

        MemberAccountEntity memberAccount = memberAccountService.getMemberAccountEntityById(memberAccountId);

        // Verificar si ya existe uno (OneToOne, solo puede haber uno)
        Optional<FuelReimbursementEntity> existing = fuelReimbursementRepository
            .findByMemberAccountId(memberAccountId);
        if (existing.isPresent()) {
            throw new InvalidDataException("Ya existe un registro de reintegro de combustible para esta cuenta");
        }

        FuelReimbursementEntity entity = convertCreateDtoToEntity(fuelReimbursement);
        entity.setMemberAccount(memberAccount);
        entity.setCreatedDate(LocalDate.now());
        entity.setActive(true);
        
        // Si no se especifica el monto acumulado, inicializar en 0
        if (entity.getAccumulatedAmount() == null) {
            entity.setAccumulatedAmount(0.0);
        }

        return convertToDTO(fuelReimbursementRepository.save(entity));
    }

    /**
     * Obtiene un reintegro de combustible por ID.
     */
    public FuelReimbursementDTO getFuelReimbursementById(Long id) {
        FuelReimbursementEntity fuelReimbursement = fuelReimbursementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Reintegro de Combustible"));
        return convertToDTO(fuelReimbursement);
    }

    /**
     * Obtiene el reintegro de combustible para una cuenta de socio.
     * Con OneToOne, solo puede haber uno por cuenta.
     */
    public FuelReimbursementDTO getFuelReimbursementByMemberAccountId(Long memberAccountId) {
        if (memberAccountId == null) {
            throw new InvalidDataException("El ID de la cuenta de socio no puede ser nulo");
        }
        FuelReimbursementEntity fuelReimbursement = fuelReimbursementRepository
            .findByMemberAccountId(memberAccountId)
            .orElseThrow(() -> new ResourceNotFoundException(memberAccountId, "Reintegro de Combustible para la cuenta"));
        return convertToDTO(fuelReimbursement);
    }

    /**
     * Lista todos los reintegros de combustible.
     */
    public List<FuelReimbursementDTO> getAllFuelReimbursements() {
        return fuelReimbursementRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene el reintegro de combustible para una cuenta de socio.
     * Con OneToOne, solo puede haber uno por cuenta.
     * Este método es un alias de getFuelReimbursementByMemberAccountId para mantener compatibilidad.
     */
    public FuelReimbursementDTO getFuelReimbursementsByMemberAccountId(Long memberAccountId) {
        return getFuelReimbursementByMemberAccountId(memberAccountId);
    }

    /**
     * Acumula un monto al reintegro de combustible de un chofer.
     * Si no existe un registro activo, lo crea automáticamente.
     */
    @Transactional
    public FuelReimbursementDTO accumulateFuelCredit(Long memberAccountId, Double amount) {
        if (memberAccountId == null) {
            throw new InvalidDataException("El ID de la cuenta de socio no puede ser nulo");
        }
        if (amount == null || amount <= 0) {
            throw new InvalidDataException("El monto a acumular debe ser positivo");
        }

        Optional<FuelReimbursementEntity> existing = fuelReimbursementRepository
            .findByMemberAccountId(memberAccountId);

        FuelReimbursementEntity fuelReimbursement;
        if (existing.isPresent()) {
            fuelReimbursement = existing.get();
            fuelReimbursement.setAccumulatedAmount(fuelReimbursement.getAccumulatedAmount() + amount);
        } else {
            // Crear nuevo registro si no existe
            MemberAccountEntity memberAccount = memberAccountService.getMemberAccountEntityById(memberAccountId);
            fuelReimbursement = FuelReimbursementEntity.builder()
                .memberAccount(memberAccount)
                .accumulatedAmount(amount)
                .createdDate(LocalDate.now())
                .active(true)
                .build();
        }

        return convertToDTO(fuelReimbursementRepository.save(fuelReimbursement));
    }

    /**
     * Reintegra el monto acumulado al balance de la cuenta de socio.
     * Suma el accumulatedAmount al balance de la MemberAccount y resetea el accumulatedAmount a 0.
     */
    @Transactional
    public FuelReimbursementDTO reimburseFuelCredit(Long memberAccountId) {
        if (memberAccountId == null) {
            throw new InvalidDataException("El ID de la cuenta de socio no puede ser nulo");
        }

        FuelReimbursementEntity fuelReimbursement = fuelReimbursementRepository
            .findByMemberAccountId(memberAccountId)
            .orElseThrow(() -> new ResourceNotFoundException(memberAccountId, "Reintegro de Combustible para la cuenta"));

        if (fuelReimbursement.getAccumulatedAmount() == null || fuelReimbursement.getAccumulatedAmount() <= 0) {
            throw new InvalidDataException("No hay monto acumulado para reintegrar");
        }

        // Obtener la cuenta de socio y sumar el monto acumulado al balance
        MemberAccountEntity memberAccount = fuelReimbursement.getMemberAccount();
        memberAccount.setBalance(memberAccount.getBalance() + fuelReimbursement.getAccumulatedAmount());
        memberAccount.setLastModified(LocalDate.now());
        memberAccountService.updateAccountEntity(memberAccount);

        // Resetear el monto acumulado y actualizar fecha de último reintegro
        fuelReimbursement.setAccumulatedAmount(0.0);
        fuelReimbursement.setLastReimbursementDate(LocalDate.now());

        return convertToDTO(fuelReimbursementRepository.save(fuelReimbursement));
    }

    /**
     * Actualiza un reintegro de combustible.
     */
    @Transactional
    public FuelReimbursementDTO updateFuelReimbursement(FuelReimbursementDTO fuelReimbursement) {
        if (fuelReimbursement.getId() == null) {
            throw new InvalidDataException("El ID no puede ser nulo para actualizar");
        }

        FuelReimbursementEntity entity = fuelReimbursementRepository.findById(fuelReimbursement.getId())
            .orElseThrow(() -> new ResourceNotFoundException(fuelReimbursement.getId(), "Reintegro de Combustible"));

        fuelReimbursementValidator.validateFuelReimbursementFields(fuelReimbursement);

        MemberAccountEntity memberAccount = memberAccountService.getMemberAccountEntityById(fuelReimbursement.getMemberAccountId());

        entity.setMemberAccount(memberAccount);
        entity.setAccumulatedAmount(fuelReimbursement.getAccumulatedAmount());
        entity.setLastReimbursementDate(fuelReimbursement.getLastReimbursementDate());
        entity.setCreatedDate(fuelReimbursement.getCreatedDate());
        entity.setActive(fuelReimbursement.getActive() != null ? fuelReimbursement.getActive() : true);

        return convertToDTO(fuelReimbursementRepository.save(entity));
    }

    /**
     * Soft delete de un reintegro de combustible.
     */
    @Transactional
    public void deleteFuelReimbursement(Long id) {
        if (id == null) {
            throw new InvalidDataException("El ID no puede ser nulo");
        }

        FuelReimbursementEntity entity = fuelReimbursementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Reintegro de Combustible"));

        entity.setActive(false);
        fuelReimbursementRepository.save(entity);
    }

    /**
     * Método auxiliar para obtener la entidad por ID (para uso interno).
     */
    public FuelReimbursementEntity getFuelReimbursementEntityById(Long id) {
        return fuelReimbursementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Reintegro de Combustible"));
    }

    /**
     * Método auxiliar para obtener la entidad por memberAccountId (para uso interno).
     * Con OneToOne, solo puede haber uno por cuenta.
     */
    public Optional<FuelReimbursementEntity> getFuelReimbursementEntityByMemberAccountId(Long memberAccountId) {
        return fuelReimbursementRepository.findByMemberAccountId(memberAccountId);
    }

    private FuelReimbursementEntity convertCreateDtoToEntity(FuelReimbursementCreateDTO dto) {
        return FuelReimbursementEntity.builder()
            .accumulatedAmount(dto.getAccumulatedAmount() != null ? dto.getAccumulatedAmount() : 0.0)
            .build();
    }

    private FuelReimbursementDTO convertToDTO(FuelReimbursementEntity entity) {
        if (entity == null) {
            return null;
        }

        return FuelReimbursementDTO.builder()
            .id(entity.getId())
            .memberAccountId(entity.getMemberAccount().getId())
            .memberAccount(memberAccountService.getMemberAccountById(entity.getMemberAccount().getId()))
            .accumulatedAmount(entity.getAccumulatedAmount())
            .lastReimbursementDate(entity.getLastReimbursementDate())
            .createdDate(entity.getCreatedDate())
            .active(entity.getActive())
            .build();
    }
}

