package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.account.VehicleAccountDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.account.VehicleAccountCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.VehicleAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.VehicleEntity;
import com.pepotec.cooperative_taxi_managment.repositories.VehicleAccountRepository;
import com.pepotec.cooperative_taxi_managment.validators.VehicleAccountValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleAccountService {

    @Autowired
    private VehicleAccountRepository vehicleAccountRepository;

    @Autowired
    @org.springframework.context.annotation.Lazy
    private VehicleService vehicleService;

    @Autowired
    private VehicleAccountValidator vehicleAccountValidator;

    public VehicleAccountDTO createVehicleAccount(Long vehicleId, VehicleAccountCreateDTO account) {
        vehicleAccountValidator.validateVehicleAccountCreateFields(account.getBalance(), account.getLastModified());

        VehicleEntity vehicle = vehicleService.getVehicleEntityById(vehicleId);

        vehicleAccountValidator.validateUniqueVehicleAccount(vehicle.getId());

        VehicleAccountEntity entity = convertCreateDtoToEntity(account);
        entity.setVehicle(vehicle);

        if (entity.getLastModified() == null) {
            entity.setLastModified(LocalDate.now());
        }

        return convertToDTO(vehicleAccountRepository.save(entity));
    }

    public VehicleAccountDTO getVehicleAccountById(Long id) {
        VehicleAccountEntity account = vehicleAccountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Cuenta de Vehículo"));
        return convertToDTO(account);
    }

    public VehicleAccountDTO getVehicleAccountByVehicleId(Long vehicleId) {
        vehicleAccountValidator.validateVehicleIdNotNull(vehicleId);
        VehicleAccountEntity account = vehicleAccountRepository.findByVehicleIdAndActiveTrue(vehicleId)
            .orElseThrow(() -> new ResourceNotFoundException(vehicleId, "Cuenta de Vehículo para el vehículo"));
        return convertToDTO(account);
    }

    public List<VehicleAccountDTO> getAllVehicleAccounts() {
        return vehicleAccountRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<VehicleAccountDTO> getActiveVehicleAccounts() {
        return vehicleAccountRepository.findByActiveTrue().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public VehicleAccountDTO updateVehicleAccount(VehicleAccountDTO account) {
        vehicleAccountValidator.validateIdNotNullForUpdate(account.getId());

        VehicleAccountEntity entity = vehicleAccountRepository.findById(account.getId())
            .orElseThrow(() -> new ResourceNotFoundException(account.getId(), "Cuenta de Vehículo"));

        vehicleAccountValidator.validateVehicleAccountFields(account);

        VehicleEntity vehicle = vehicleService.getVehicleEntityById(account.getVehicleId());

        entity.setVehicle(vehicle);
        entity.setBalance(account.getBalance());
        entity.setLastModified(account.getLastModified() != null ? account.getLastModified() : LocalDate.now());

        return convertToDTO(vehicleAccountRepository.save(entity));
    }

    public void deleteVehicleAccount(Long id) {
        vehicleAccountValidator.validateIdNotNull(id);

        VehicleAccountEntity entity = vehicleAccountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Cuenta de Vehículo"));

        entity.setActive(false);
        entity.setLastModified(LocalDate.now());
        vehicleAccountRepository.save(entity);
    }

    private VehicleAccountEntity convertToEntity(VehicleAccountDTO dto) {
        VehicleAccountEntity entity = VehicleAccountEntity.builder()
            .id(dto.getId())
            .balance(dto.getBalance())
            .lastModified(dto.getLastModified())
            .build();
        return entity;
    }

    private VehicleAccountEntity convertCreateDtoToEntity(VehicleAccountCreateDTO dto) {
        VehicleAccountEntity entity = VehicleAccountEntity.builder()
            .balance(dto.getBalance())
            .lastModified(dto.getLastModified())
            .active(true)
            .build();
        return entity;
    }

    private VehicleAccountDTO convertToDTO(VehicleAccountEntity entity) {
        if (entity == null) {
            return null;
        }

        return VehicleAccountDTO.builder()
            .id(entity.getId())
            .vehicleId(entity.getVehicle().getId())
            .vehicle(vehicleService.getVehicleById(entity.getVehicle().getId()))
            .balance(entity.getBalance())
            .lastModified(entity.getLastModified())
            .active(entity.getActive())
            .build();
    }

    /**
     * Método auxiliar para actualizar la entidad directamente (para uso interno).
     */
    public VehicleAccountEntity updateAccountEntity(VehicleAccountEntity entity) {
        return vehicleAccountRepository.save(entity);
    }

    /**
     * Método auxiliar para obtener la entidad por ID (uso interno).
     */
    public VehicleAccountEntity getVehicleAccountEntityById(Long id) {
        return vehicleAccountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Cuenta de Vehículo"));
    }
}


