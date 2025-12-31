package com.pepotec.cooperative_taxi_managment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.VehicleDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.VehicleCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.VehicleEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.ModelEntity;
import com.pepotec.cooperative_taxi_managment.repositories.VehicleRepository;
import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.validators.VehicleValidator;
import com.pepotec.cooperative_taxi_managment.models.dto.vehicle.account.VehicleAccountCreateDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ModelService modelService;

    @Autowired
    private VehicleValidator vehicleValidator;

    @Autowired
    private VehicleAccountService vehicleAccountService;

    public VehicleDTO createVehicle(VehicleCreateDTO vehicle) {
        vehicleValidator.validateVehicleCreateFields(vehicle);
        // Para unicidad usamos los valores del createDTO
        vehicleValidator.validateUniqueFields(
            VehicleDTO.builder()
                .licensePlate(vehicle.getLicensePlate())
                .licenseNumber(vehicle.getLicenseNumber())
                .engineNumber(vehicle.getEngineNumber())
                .chassisNumber(vehicle.getChassisNumber())
                .vtvExpirationDate(vehicle.getVtvExpirationDate())
                .active(true)
                .model(null)
                .build(),
            null
        );

        ModelEntity model = modelService.getModelEntityById(vehicle.getModelId());

        VehicleEntity vehicleEntity = VehicleEntity.builder()
            .licensePlate(vehicle.getLicensePlate())
            .licenseNumber(vehicle.getLicenseNumber())
            .engineNumber(vehicle.getEngineNumber())
            .chassisNumber(vehicle.getChassisNumber())
            .vtvExpirationDate(vehicle.getVtvExpirationDate())
            .active(true)
            .leaveDate(null)
            .build();
        vehicleEntity.setModel(model);

        vehicleEntity = vehicleRepository.save(vehicleEntity);

        // Crear cuenta asociada con saldo 0
        VehicleAccountCreateDTO accountCreateDTO = VehicleAccountCreateDTO.builder()
            .balance(0.0)
            .lastModified(null)
            .build();
        vehicleAccountService.createVehicleAccount(vehicleEntity.getId(), accountCreateDTO);

        return convertToDTO(vehicleEntity);
    }

    public VehicleDTO getVehicleById(Long id) {
        VehicleEntity vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Vehículo"));
        return convertToDTO(vehicle);
    }

    public VehicleDTO getVehicleByLicensePlate(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new InvalidDataException("The license plate cannot be empty");
        }

        VehicleEntity vehicle = vehicleRepository.findByLicensePlate(licensePlate)
            .orElseThrow(() -> new ResourceNotFoundException(null, "Vehículo con patente " + licensePlate));
        return convertToDTO(vehicle);
    }

    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<VehicleDTO> getActiveVehicles() {
        return vehicleRepository.findByActiveTrueAndLeaveDateIsNull().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<VehicleDTO> getVehiclesByModel(Long modelId) {
        return vehicleRepository.findByModelId(modelId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public VehicleDTO updateVehicle(VehicleDTO vehicle) {
        if (vehicle.getId() == null) {
            throw new InvalidDataException("The ID cannot be null for update");
        }

        VehicleEntity vehicleEntity = vehicleRepository.findById(vehicle.getId())
            .orElseThrow(() -> new ResourceNotFoundException(vehicle.getId(), "Vehículo"));

        vehicleValidator.validateVehicleSpecificFields(vehicle);
        vehicleValidator.validateUniqueFields(vehicle, vehicle.getId());

        ModelEntity model = modelService.getModelEntityById(vehicle.getModel().getId());

        vehicleEntity.setLicensePlate(vehicle.getLicensePlate());
        vehicleEntity.setLicenseNumber(vehicle.getLicenseNumber());
        vehicleEntity.setEngineNumber(vehicle.getEngineNumber());
        vehicleEntity.setChassisNumber(vehicle.getChassisNumber());
        vehicleEntity.setVtvExpirationDate(vehicle.getVtvExpirationDate());
        vehicleEntity.setModel(model);
        vehicleEntity.setActive(vehicle.getActive() != null ? vehicle.getActive() : true);

        return convertToDTO(vehicleRepository.save(vehicleEntity));
    }

    public void deleteVehicle(Long id) {
        if (id == null) {
            throw new InvalidDataException("The ID cannot be null");
        }

        VehicleEntity vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Vehículo"));

        vehicle.setActive(false);
        vehicle.setLeaveDate(LocalDate.now());
        vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id, LocalDate leaveDate) {
        if (id == null) {
            throw new InvalidDataException("The ID cannot be null");
        }

        VehicleEntity vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Vehículo"));

        vehicle.setActive(false);
        vehicle.setLeaveDate(leaveDate);
        vehicleRepository.save(vehicle);
    }

    private VehicleEntity convertToEntity(VehicleDTO vehicle) {
        return VehicleEntity.builder()
            .id(vehicle.getId())
            .licensePlate(vehicle.getLicensePlate())
            .licenseNumber(vehicle.getLicenseNumber())
            .engineNumber(vehicle.getEngineNumber())
            .chassisNumber(vehicle.getChassisNumber())
            .vtvExpirationDate(vehicle.getVtvExpirationDate())
            .active(vehicle.getActive() != null ? vehicle.getActive() : true)
            .leaveDate(vehicle.getLeaveDate())
            .build();
    }

    private VehicleDTO convertToDTO(VehicleEntity vehicle) {
        if (vehicle == null) {
            return null;
        }
        return VehicleDTO.builder()
            .id(vehicle.getId())
            .licensePlate(vehicle.getLicensePlate())
            .licenseNumber(vehicle.getLicenseNumber())
            .engineNumber(vehicle.getEngineNumber())
            .chassisNumber(vehicle.getChassisNumber())
            .vtvExpirationDate(vehicle.getVtvExpirationDate())
            .active(vehicle.getActive())
            .leaveDate(vehicle.getLeaveDate())
            .model(modelService.getModelById(vehicle.getModel().getId()))
            .build();
    }

    /**
     * Obtiene la entidad VehicleEntity por ID para uso interno de otros servicios.
     * Este método es package-private para mantener el encapsulamiento.
     */
    VehicleEntity getVehicleEntityById(Long id) {
        return vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Vehículo"));
    }
}

