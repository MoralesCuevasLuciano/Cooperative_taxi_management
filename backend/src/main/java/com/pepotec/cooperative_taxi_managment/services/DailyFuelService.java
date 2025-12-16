package com.pepotec.cooperative_taxi_managment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pepotec.cooperative_taxi_managment.models.dto.dailyfuel.DailyFuelDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.dailyfuel.DailyFuelCreateDTO;
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

    @Autowired
    private FuelReimbursementService fuelReimbursementService;

    @Autowired
    private MemberAccountService memberAccountService;

    @Transactional
    public DailyFuelDTO createDailyFuel(Long driverId, Long vehicleId, Long settlementId, DailyFuelCreateDTO dailyFuel) {
        dailyFuelValidator.validateDailyFuelCreateFields(dailyFuel);

        DriverEntity driver = driverService.getDriverEntityById(driverId);
        VehicleEntity vehicle = vehicleService.getVehicleEntityById(vehicleId);

        DailyFuelEntity dailyFuelEntity = convertCreateDtoToEntity(dailyFuel);
        dailyFuelEntity.setDriver(driver);
        dailyFuelEntity.setVehicle(vehicle);

        if (settlementId == null) {
            throw new InvalidDataException("El ID de rendición no puede ser nulo");
        }
        var settlement = driverSettlementService.getDriverSettlementEntityById(settlementId);
        dailyFuelEntity.setSettlement(settlement);

        // Asignar porcentajes por defecto si no se especificaron
        assignDefaultPercentages(dailyFuelEntity, driverId, dailyFuel.getFuelType());

        // Guardar el DailyFuel
        dailyFuelEntity = dailyFuelRepository.save(dailyFuelEntity);

        // Acumular crédito del chofer si hay porcentaje del chofer
        if (dailyFuelEntity.getDriverPercentage() != null && dailyFuelEntity.getDriverPercentage() > 0) {
            accumulateDriverFuelCredit(driverId, dailyFuelEntity.getAmount(), dailyFuelEntity.getDriverPercentage());
        }

        return convertToDTO(dailyFuelEntity);
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
        dailyFuelEntity.setCooperativePercentage(dailyFuel.getCooperativePercentage());
        dailyFuelEntity.setDriverPercentage(dailyFuel.getDriverPercentage());
        
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

    /**
     * Asigna porcentajes por defecto si no se especificaron.
     * Busca el último DailyFuel del mismo tipo para ese chofer, si no existe usa 50/50.
     */
    private void assignDefaultPercentages(DailyFuelEntity dailyFuelEntity, Long driverId, FuelType fuelType) {
        // Si ya tiene porcentajes asignados, no hacer nada
        if (dailyFuelEntity.getCooperativePercentage() != null && dailyFuelEntity.getDriverPercentage() != null) {
            return;
        }

        // Buscar el último DailyFuel del mismo tipo para ese chofer
        var lastDailyFuel = dailyFuelRepository.findFirstByDriverIdAndFuelTypeOrderByTicketIssueDateDesc(driverId, fuelType);

        if (lastDailyFuel.isPresent() && 
            lastDailyFuel.get().getCooperativePercentage() != null && 
            lastDailyFuel.get().getDriverPercentage() != null) {
            // Usar los porcentajes del último DailyFuel del mismo tipo
            dailyFuelEntity.setCooperativePercentage(lastDailyFuel.get().getCooperativePercentage());
            dailyFuelEntity.setDriverPercentage(lastDailyFuel.get().getDriverPercentage());
        } else {
            // Usar 50/50 por defecto
            dailyFuelEntity.setCooperativePercentage(50.0);
            dailyFuelEntity.setDriverPercentage(50.0);
        }
    }

    /**
     * Acumula el crédito de combustible del chofer en su FuelReimbursement.
     */
    private void accumulateDriverFuelCredit(Long driverId, Double amount, Double driverPercentage) {
        try {
            // Obtener la cuenta del chofer (Driver extiende Member, así que tiene MemberAccount)
            var memberAccount = memberAccountService.getMemberAccountByMemberId(driverId);
            
            // Calcular el crédito del chofer
            Double driverCredit = amount * (driverPercentage / 100.0);
            
            // Acumular en FuelReimbursement
            fuelReimbursementService.accumulateFuelCredit(memberAccount.getId(), driverCredit);
        } catch (ResourceNotFoundException e) {
            // Si no existe la cuenta, no acumular (no debería pasar, pero por seguridad)
            // Log podría ir aquí en producción
        }
    }

    private DailyFuelEntity convertToEntity(DailyFuelDTO dailyFuel) {
        DailyFuelEntity entity = DailyFuelEntity.builder()
            .id(dailyFuel.getId())
            .ticketIssueDate(dailyFuel.getTicketIssueDate())
            .submissionDate(dailyFuel.getSubmissionDate())
            .amount(dailyFuel.getAmount())
            .fuelType(dailyFuel.getFuelType())
            .cooperativePercentage(dailyFuel.getCooperativePercentage())
            .driverPercentage(dailyFuel.getDriverPercentage())
            .build();
        
        if (dailyFuel.getSettlement() != null && dailyFuel.getSettlement().getId() != null) {
            var settlement = driverSettlementService.getDriverSettlementEntityById(dailyFuel.getSettlement().getId());
            entity.setSettlement(settlement);
        }
        
        return entity;
    }

    private DailyFuelEntity convertCreateDtoToEntity(DailyFuelCreateDTO dailyFuel) {
        DailyFuelEntity entity = DailyFuelEntity.builder()
            .ticketIssueDate(dailyFuel.getTicketIssueDate())
            .submissionDate(dailyFuel.getSubmissionDate())
            .amount(dailyFuel.getAmount())
            .fuelType(dailyFuel.getFuelType())
            .cooperativePercentage(dailyFuel.getCooperativePercentage())
            .driverPercentage(dailyFuel.getDriverPercentage())
            .build();

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
            .fuelType(dailyFuel.getFuelType())
            .cooperativePercentage(dailyFuel.getCooperativePercentage())
            .driverPercentage(dailyFuel.getDriverPercentage());
        
        if (dailyFuel.getSettlement() != null) {
            builder.settlement(driverSettlementService.getDriverSettlementById(dailyFuel.getSettlement().getId()));
        }
        
        return builder.build();
    }
}

