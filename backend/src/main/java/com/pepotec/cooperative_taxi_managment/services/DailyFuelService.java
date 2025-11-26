package com.pepotec.cooperative_taxi_managment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pepotec.cooperative_taxi_managment.models.dto.DailyFuelDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.DailyFuelEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.DriverEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.VehicleEntity;
import com.pepotec.cooperative_taxi_managment.repositories.DailyFuelRepository;
import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.validators.DailyFuelValidator;
import com.pepotec.cooperative_taxi_managment.models.enums.FuelType;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyFuelService {

    @Autowired
    private DailyFuelRepository dailyFuelRepository;

    @Autowired
    private DriverService driverService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private DriverSettlementService driverSettlementService;

    @Autowired
    private DailyFuelValidator dailyFuelValidator;

    @Transactional
    public DailyFuelDTO createDailyFuel(DailyFuelDTO dailyFuel) {
        dailyFuelValidator.validateDailyFuelSpecificFields(dailyFuel);

        DriverEntity driver = driverService.getDriverEntityById(dailyFuel.getDriver().getId());
        VehicleEntity vehicle = vehicleService.getVehicleEntityById(dailyFuel.getVehicle().getId());

        DailyFuelEntity dailyFuelEntity = convertToEntity(dailyFuel);
        dailyFuelEntity.setDriver(driver);
        dailyFuelEntity.setVehicle(vehicle);
        
        if (dailyFuel.getSettlement() != null && dailyFuel.getSettlement().getId() != null) {
            var settlement = driverSettlementService.getDriverSettlementEntityById(dailyFuel.getSettlement().getId());
            dailyFuelEntity.setSettlement(settlement);
        }

        return convertToDTO(dailyFuelRepository.save(dailyFuelEntity));
    }

    public DailyFuelDTO getDailyFuelById(Long id) {
        DailyFuelEntity dailyFuel = dailyFuelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Combustible Diario"));
        return convertToDTO(dailyFuel);
    }

    public List<DailyFuelDTO> getAllDailyFuels() {
        return dailyFuelRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DailyFuelDTO> getDailyFuelsByVehicle(Long vehicleId) {
        if (vehicleId == null) {
            throw new InvalidDataException("El ID del vehículo no puede ser nulo");
        }
        return dailyFuelRepository.findByVehicleId(vehicleId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DailyFuelDTO> getDailyFuelsByDriver(Long driverId) {
        if (driverId == null) {
            throw new InvalidDataException("El ID del chofer no puede ser nulo");
        }
        return dailyFuelRepository.findByDriverId(driverId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DailyFuelDTO> getDailyFuelsByTicketIssueDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return dailyFuelRepository.findByTicketIssueDateBetween(startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DailyFuelDTO> getDailyFuelsBySubmissionDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return dailyFuelRepository.findBySubmissionDateBetween(startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DailyFuelDTO> getDailyFuelsByFuelType(FuelType fuelType) {
        if (fuelType == null) {
            throw new InvalidDataException("El tipo de combustible no puede ser nulo");
        }
        return dailyFuelRepository.findByFuelType(fuelType).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DailyFuelDTO> getDailyFuelsByVehicleAndTicketIssueDateRange(Long vehicleId, LocalDate startDate, LocalDate endDate) {
        if (vehicleId == null) {
            throw new InvalidDataException("El ID del vehículo no puede ser nulo");
        }
        if (startDate == null || endDate == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return dailyFuelRepository.findByVehicleIdAndTicketIssueDateBetween(vehicleId, startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DailyFuelDTO> getDailyFuelsByDriverAndTicketIssueDateRange(Long driverId, LocalDate startDate, LocalDate endDate) {
        if (driverId == null) {
            throw new InvalidDataException("El ID del chofer no puede ser nulo");
        }
        if (startDate == null || endDate == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return dailyFuelRepository.findByDriverIdAndTicketIssueDateBetween(driverId, startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DailyFuelDTO> getDailyFuelsByVehicleAndFuelType(Long vehicleId, FuelType fuelType) {
        if (vehicleId == null) {
            throw new InvalidDataException("El ID del vehículo no puede ser nulo");
        }
        if (fuelType == null) {
            throw new InvalidDataException("El tipo de combustible no puede ser nulo");
        }
        return dailyFuelRepository.findByVehicleIdAndFuelType(vehicleId, fuelType).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DailyFuelDTO> getDailyFuelsByDriverAndFuelType(Long driverId, FuelType fuelType) {
        if (driverId == null) {
            throw new InvalidDataException("El ID del chofer no puede ser nulo");
        }
        if (fuelType == null) {
            throw new InvalidDataException("El tipo de combustible no puede ser nulo");
        }
        return dailyFuelRepository.findByDriverIdAndFuelType(driverId, fuelType).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public DailyFuelDTO updateDailyFuel(DailyFuelDTO dailyFuel) {
        if (dailyFuel.getId() == null) {
            throw new InvalidDataException("El ID no puede ser nulo para actualizar");
        }

        DailyFuelEntity dailyFuelEntity = dailyFuelRepository.findById(dailyFuel.getId())
            .orElseThrow(() -> new ResourceNotFoundException(dailyFuel.getId(), "Combustible Diario"));

        dailyFuelValidator.validateDailyFuelSpecificFields(dailyFuel);

        DriverEntity driver = driverService.getDriverEntityById(dailyFuel.getDriver().getId());
        VehicleEntity vehicle = vehicleService.getVehicleEntityById(dailyFuel.getVehicle().getId());

        dailyFuelEntity.setDriver(driver);
        dailyFuelEntity.setVehicle(vehicle);
        dailyFuelEntity.setTicketIssueDate(dailyFuel.getTicketIssueDate());
        dailyFuelEntity.setSubmissionDate(dailyFuel.getSubmissionDate());
        dailyFuelEntity.setAmount(dailyFuel.getAmount());
        dailyFuelEntity.setFuelType(dailyFuel.getFuelType());
        
        if (dailyFuel.getSettlement() != null && dailyFuel.getSettlement().getId() != null) {
            var settlement = driverSettlementService.getDriverSettlementEntityById(dailyFuel.getSettlement().getId());
            dailyFuelEntity.setSettlement(settlement);
        } else {
            dailyFuelEntity.setSettlement(null);
        }

        return convertToDTO(dailyFuelRepository.save(dailyFuelEntity));
    }

    public void deleteDailyFuel(Long id) {
        if (id == null) {
            throw new InvalidDataException("El ID no puede ser nulo");
        }

        DailyFuelEntity dailyFuel = dailyFuelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Combustible Diario"));

        dailyFuelRepository.delete(dailyFuel);
    }

    private DailyFuelEntity convertToEntity(DailyFuelDTO dailyFuel) {
        DailyFuelEntity entity = DailyFuelEntity.builder()
            .id(dailyFuel.getId())
            .ticketIssueDate(dailyFuel.getTicketIssueDate())
            .submissionDate(dailyFuel.getSubmissionDate())
            .amount(dailyFuel.getAmount())
            .fuelType(dailyFuel.getFuelType())
            .build();
        
        if (dailyFuel.getSettlement() != null && dailyFuel.getSettlement().getId() != null) {
            var settlement = driverSettlementService.getDriverSettlementEntityById(dailyFuel.getSettlement().getId());
            entity.setSettlement(settlement);
        }
        
        return entity;
    }

    private DailyFuelDTO convertToDTO(DailyFuelEntity dailyFuel) {
        if (dailyFuel == null) {
            return null;
        }
        
        DailyFuelDTO.DailyFuelDTOBuilder builder = DailyFuelDTO.builder()
            .id(dailyFuel.getId())
            .driver(driverService.getDriverById(dailyFuel.getDriver().getId()))
            .vehicle(vehicleService.getVehicleById(dailyFuel.getVehicle().getId()))
            .ticketIssueDate(dailyFuel.getTicketIssueDate())
            .submissionDate(dailyFuel.getSubmissionDate())
            .amount(dailyFuel.getAmount())
            .fuelType(dailyFuel.getFuelType());
        
        if (dailyFuel.getSettlement() != null) {
            builder.settlement(driverSettlementService.getDriverSettlementById(dailyFuel.getSettlement().getId()));
        }
        
        return builder.build();
    }
}

