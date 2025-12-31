package com.pepotec.cooperative_taxi_managment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pepotec.cooperative_taxi_managment.models.dto.tickettaxi.TicketTaxiDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.tickettaxi.TicketTaxiCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.TicketTaxiEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.VehicleEntity;
import com.pepotec.cooperative_taxi_managment.repositories.TicketTaxiRepository;
import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.validators.TicketTaxiValidator;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketTaxiService {

    @Autowired
    private TicketTaxiRepository ticketTaxiRepository;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private DriverSettlementService driverSettlementService;

    @Autowired
    private TicketTaxiValidator ticketTaxiValidator;

    @Transactional
    public TicketTaxiDTO createTicketTaxi(Long settlementId, Long vehicleId, TicketTaxiCreateDTO ticketTaxi) {
        ticketTaxiValidator.validateTicketTaxiCreateFields(ticketTaxi);

        VehicleEntity vehicle = vehicleService.getVehicleEntityById(vehicleId);
        var settlement = driverSettlementService.getDriverSettlementEntityById(settlementId);

        TicketTaxiEntity ticketTaxiEntity = convertCreateDtoToEntity(ticketTaxi);
        ticketTaxiEntity.setVehicle(vehicle);
        ticketTaxiEntity.setSettlement(settlement);

        return convertToDTO(ticketTaxiRepository.save(ticketTaxiEntity));
    }

    public TicketTaxiDTO getTicketTaxiById(Long id) {
        TicketTaxiEntity ticketTaxi = ticketTaxiRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Ticket de Taxi"));
        return convertToDTO(ticketTaxi);
    }

    public TicketTaxiDTO getTicketTaxiByTicketNumber(String ticketNumber) {
        ticketTaxiValidator.validateTicketNumberNotEmpty(ticketNumber);

        TicketTaxiEntity ticketTaxi = ticketTaxiRepository.findByTicketNumber(ticketNumber)
            .orElseThrow(() -> new ResourceNotFoundException(null, "Ticket de Taxi con número " + ticketNumber));
        return convertToDTO(ticketTaxi);
    }

    public List<TicketTaxiDTO> getAllTicketTaxis() {
        return ticketTaxiRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisByVehicle(Long vehicleId) {
        ticketTaxiValidator.validateVehicleIdNotNull(vehicleId);
        return ticketTaxiRepository.findByVehicleId(vehicleId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisBySettlement(Long settlementId) {
        ticketTaxiValidator.validateSettlementIdNotNull(settlementId);
        return ticketTaxiRepository.findBySettlementId(settlementId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisByStartDateRange(LocalDate startDate, LocalDate endDate) {
        ticketTaxiValidator.validateDateRange(startDate, endDate);
        return ticketTaxiRepository.findByStartDateBetween(startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisByCutDateRange(LocalDate startDate, LocalDate endDate) {
        ticketTaxiValidator.validateDateRange(startDate, endDate);
        return ticketTaxiRepository.findByCutDateBetween(startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisByVehicleAndStartDateRange(Long vehicleId, LocalDate startDate, LocalDate endDate) {
        ticketTaxiValidator.validateVehicleIdNotNull(vehicleId);
        ticketTaxiValidator.validateDateRange(startDate, endDate);
        return ticketTaxiRepository.findByVehicleIdAndStartDateBetween(vehicleId, startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisByVehicleAndCutDateRange(Long vehicleId, LocalDate startDate, LocalDate endDate) {
        ticketTaxiValidator.validateVehicleIdNotNull(vehicleId);
        ticketTaxiValidator.validateDateRange(startDate, endDate);
        return ticketTaxiRepository.findByVehicleIdAndCutDateBetween(vehicleId, startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisBySettlementAndStartDateRange(Long settlementId, LocalDate startDate, LocalDate endDate) {
        ticketTaxiValidator.validateSettlementIdNotNull(settlementId);
        ticketTaxiValidator.validateDateRange(startDate, endDate);
        return ticketTaxiRepository.findBySettlementIdAndStartDateBetween(settlementId, startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisBySettlementAndCutDateRange(Long settlementId, LocalDate startDate, LocalDate endDate) {
        ticketTaxiValidator.validateSettlementIdNotNull(settlementId);
        ticketTaxiValidator.validateDateRange(startDate, endDate);
        return ticketTaxiRepository.findBySettlementIdAndCutDateBetween(settlementId, startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public TicketTaxiDTO updateTicketTaxi(TicketTaxiDTO ticketTaxi) {
        ticketTaxiValidator.validateIdNotNullForUpdate(ticketTaxi.getId());

        TicketTaxiEntity ticketTaxiEntity = ticketTaxiRepository.findById(ticketTaxi.getId())
            .orElseThrow(() -> new ResourceNotFoundException(ticketTaxi.getId(), "Ticket de Taxi"));

        ticketTaxiValidator.validateTicketTaxiSpecificFields(ticketTaxi);

        VehicleEntity vehicle = vehicleService.getVehicleEntityById(ticketTaxi.getVehicle().getId());
        var settlement = driverSettlementService.getDriverSettlementEntityById(ticketTaxi.getSettlement().getId());

        ticketTaxiEntity.setVehicle(vehicle);
        ticketTaxiEntity.setSettlement(settlement);
        ticketTaxiEntity.setTicketNumber(ticketTaxi.getTicketNumber());
        ticketTaxiEntity.setStartDate(ticketTaxi.getStartDate());
        ticketTaxiEntity.setCutDate(ticketTaxi.getCutDate());
        ticketTaxiEntity.setAmount(ticketTaxi.getAmount());
        ticketTaxiEntity.setFreeKilometers(ticketTaxi.getFreeKilometers());
        ticketTaxiEntity.setOccupiedKilometers(ticketTaxi.getOccupiedKilometers());
        ticketTaxiEntity.setTrips(ticketTaxi.getTrips());

        return convertToDTO(ticketTaxiRepository.save(ticketTaxiEntity));
    }

    public void deleteTicketTaxi(Long id) {
        ticketTaxiValidator.validateIdNotNull(id);

        TicketTaxiEntity ticketTaxi = ticketTaxiRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Ticket de Taxi"));

        ticketTaxiRepository.delete(ticketTaxi);
    }

    private TicketTaxiEntity convertToEntity(TicketTaxiDTO ticketTaxi) {
        TicketTaxiEntity entity = TicketTaxiEntity.builder()
            .id(ticketTaxi.getId())
            .ticketNumber(ticketTaxi.getTicketNumber())
            .startDate(ticketTaxi.getStartDate())
            .cutDate(ticketTaxi.getCutDate())
            .amount(ticketTaxi.getAmount())
            .freeKilometers(ticketTaxi.getFreeKilometers())
            .occupiedKilometers(ticketTaxi.getOccupiedKilometers())
            .trips(ticketTaxi.getTrips())
            .build();
        
        if (ticketTaxi.getSettlement() != null && ticketTaxi.getSettlement().getId() != null) {
            var settlement = driverSettlementService.getDriverSettlementEntityById(ticketTaxi.getSettlement().getId());
            entity.setSettlement(settlement);
        }
        
        return entity;
    }

    private TicketTaxiEntity convertCreateDtoToEntity(TicketTaxiCreateDTO ticketTaxi) {
        TicketTaxiEntity entity = TicketTaxiEntity.builder()
            .ticketNumber(ticketTaxi.getTicketNumber())
            .startDate(ticketTaxi.getStartDate())
            .cutDate(ticketTaxi.getCutDate())
            .amount(ticketTaxi.getAmount())
            .freeKilometers(ticketTaxi.getFreeKilometers())
            .occupiedKilometers(ticketTaxi.getOccupiedKilometers())
            .trips(ticketTaxi.getTrips())
            .build();

        return entity;
    }

    private TicketTaxiDTO convertToDTO(TicketTaxiEntity ticketTaxi) {
        if (ticketTaxi == null) {
            return null;
        }
        
        TicketTaxiDTO.TicketTaxiDTOBuilder builder = TicketTaxiDTO.builder()
            .id(ticketTaxi.getId())
            .vehicle(vehicleService.getVehicleById(ticketTaxi.getVehicle().getId()))
            .ticketNumber(ticketTaxi.getTicketNumber())
            .startDate(ticketTaxi.getStartDate())
            .cutDate(ticketTaxi.getCutDate())
            .amount(ticketTaxi.getAmount())
            .freeKilometers(ticketTaxi.getFreeKilometers())
            .occupiedKilometers(ticketTaxi.getOccupiedKilometers())
            .trips(ticketTaxi.getTrips());
        
        if (ticketTaxi.getSettlement() != null) {
            builder.settlement(driverSettlementService.getDriverSettlementById(ticketTaxi.getSettlement().getId()));
        }
        
        return builder.build();
    }
}

