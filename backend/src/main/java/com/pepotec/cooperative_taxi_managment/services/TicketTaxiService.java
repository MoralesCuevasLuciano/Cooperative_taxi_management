package com.pepotec.cooperative_taxi_managment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pepotec.cooperative_taxi_managment.models.dto.TicketTaxiDTO;
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
    private TicketTaxiValidator ticketTaxiValidator;

    @Transactional
    public TicketTaxiDTO createTicketTaxi(TicketTaxiDTO ticketTaxi) {
        ticketTaxiValidator.validateTicketTaxiSpecificFields(ticketTaxi);

        VehicleEntity vehicle = vehicleService.getVehicleEntityById(ticketTaxi.getVehicle().getId());

        TicketTaxiEntity ticketTaxiEntity = convertToEntity(ticketTaxi);
        ticketTaxiEntity.setVehicle(vehicle);

        return convertToDTO(ticketTaxiRepository.save(ticketTaxiEntity));
    }

    public TicketTaxiDTO getTicketTaxiById(Long id) {
        TicketTaxiEntity ticketTaxi = ticketTaxiRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Ticket de Taxi"));
        return convertToDTO(ticketTaxi);
    }

    public TicketTaxiDTO getTicketTaxiByTicketNumber(String ticketNumber) {
        if (ticketNumber == null || ticketNumber.trim().isEmpty()) {
            throw new InvalidDataException("El número de ticket no puede estar vacío");
        }

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
        if (vehicleId == null) {
            throw new InvalidDataException("El ID del vehículo no puede ser nulo");
        }
        return ticketTaxiRepository.findByVehicleId(vehicleId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisByRendicion(Long rendicionId) {
        if (rendicionId == null) {
            throw new InvalidDataException("El ID de rendición no puede ser nulo");
        }
        return ticketTaxiRepository.findByRendicionId(rendicionId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisByStartDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return ticketTaxiRepository.findByStartDateBetween(startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisByCutDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return ticketTaxiRepository.findByCutDateBetween(startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisByVehicleAndStartDateRange(Long vehicleId, LocalDate startDate, LocalDate endDate) {
        if (vehicleId == null) {
            throw new InvalidDataException("El ID del vehículo no puede ser nulo");
        }
        if (startDate == null || endDate == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return ticketTaxiRepository.findByVehicleIdAndStartDateBetween(vehicleId, startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisByVehicleAndCutDateRange(Long vehicleId, LocalDate startDate, LocalDate endDate) {
        if (vehicleId == null) {
            throw new InvalidDataException("El ID del vehículo no puede ser nulo");
        }
        if (startDate == null || endDate == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return ticketTaxiRepository.findByVehicleIdAndCutDateBetween(vehicleId, startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisByRendicionAndStartDateRange(Long rendicionId, LocalDate startDate, LocalDate endDate) {
        if (rendicionId == null) {
            throw new InvalidDataException("El ID de rendición no puede ser nulo");
        }
        if (startDate == null || endDate == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return ticketTaxiRepository.findByRendicionIdAndStartDateBetween(rendicionId, startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TicketTaxiDTO> getTicketTaxisByRendicionAndCutDateRange(Long rendicionId, LocalDate startDate, LocalDate endDate) {
        if (rendicionId == null) {
            throw new InvalidDataException("El ID de rendición no puede ser nulo");
        }
        if (startDate == null || endDate == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return ticketTaxiRepository.findByRendicionIdAndCutDateBetween(rendicionId, startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public TicketTaxiDTO updateTicketTaxi(TicketTaxiDTO ticketTaxi) {
        if (ticketTaxi.getId() == null) {
            throw new InvalidDataException("El ID no puede ser nulo para actualizar");
        }

        TicketTaxiEntity ticketTaxiEntity = ticketTaxiRepository.findById(ticketTaxi.getId())
            .orElseThrow(() -> new ResourceNotFoundException(ticketTaxi.getId(), "Ticket de Taxi"));

        ticketTaxiValidator.validateTicketTaxiSpecificFields(ticketTaxi);

        VehicleEntity vehicle = vehicleService.getVehicleEntityById(ticketTaxi.getVehicle().getId());

        ticketTaxiEntity.setVehicle(vehicle);
        ticketTaxiEntity.setRendicionId(ticketTaxi.getRendicionId());
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
        if (id == null) {
            throw new InvalidDataException("El ID no puede ser nulo");
        }

        TicketTaxiEntity ticketTaxi = ticketTaxiRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Ticket de Taxi"));

        ticketTaxiRepository.delete(ticketTaxi);
    }

    private TicketTaxiEntity convertToEntity(TicketTaxiDTO ticketTaxi) {
        return TicketTaxiEntity.builder()
            .id(ticketTaxi.getId())
            .rendicionId(ticketTaxi.getRendicionId())
            .ticketNumber(ticketTaxi.getTicketNumber())
            .startDate(ticketTaxi.getStartDate())
            .cutDate(ticketTaxi.getCutDate())
            .amount(ticketTaxi.getAmount())
            .freeKilometers(ticketTaxi.getFreeKilometers())
            .occupiedKilometers(ticketTaxi.getOccupiedKilometers())
            .trips(ticketTaxi.getTrips())
            .build();
    }

    private TicketTaxiDTO convertToDTO(TicketTaxiEntity ticketTaxi) {
        if (ticketTaxi == null) {
            return null;
        }
        return TicketTaxiDTO.builder()
            .id(ticketTaxi.getId())
            .vehicle(vehicleService.getVehicleById(ticketTaxi.getVehicle().getId()))
            .rendicionId(ticketTaxi.getRendicionId())
            .ticketNumber(ticketTaxi.getTicketNumber())
            .startDate(ticketTaxi.getStartDate())
            .cutDate(ticketTaxi.getCutDate())
            .amount(ticketTaxi.getAmount())
            .freeKilometers(ticketTaxi.getFreeKilometers())
            .occupiedKilometers(ticketTaxi.getOccupiedKilometers())
            .trips(ticketTaxi.getTrips())
            .build();
    }
}

