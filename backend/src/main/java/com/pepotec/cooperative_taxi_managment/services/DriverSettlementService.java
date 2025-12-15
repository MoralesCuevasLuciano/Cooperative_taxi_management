package com.pepotec.cooperative_taxi_managment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pepotec.cooperative_taxi_managment.models.dto.driversettlement.DriverSettlementDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.driversettlement.DriverSettlementCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.DriverSettlementEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.DriverEntity;
import com.pepotec.cooperative_taxi_managment.repositories.DriverSettlementRepository;
import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.validators.DriverSettlementValidator;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverSettlementService {

    @Autowired
    private DriverSettlementRepository driverSettlementRepository;

    @Autowired
    private DriverService driverService;

    @Autowired
    private DriverSettlementValidator driverSettlementValidator;

    @Autowired
    @Lazy
    private TicketTaxiService ticketTaxiService;

    @Transactional
    public DriverSettlementDTO createDriverSettlement(Long driverId, DriverSettlementCreateDTO settlement) {
        driverSettlementValidator.validateDriverSettlementCreateFields(settlement);

        DriverEntity driver = driverService.getDriverEntityById(driverId);

        DriverSettlementEntity settlementEntity = convertCreateDtoToEntity(settlement);
        settlementEntity.setDriver(driver);

        return convertToDTO(driverSettlementRepository.save(settlementEntity));
    }

    public DriverSettlementDTO getDriverSettlementById(Long id) {
        DriverSettlementEntity settlement = driverSettlementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Rendición de Chofer"));
        return convertToDTO(settlement);
    }

    public List<DriverSettlementDTO> getAllDriverSettlements() {
        return driverSettlementRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DriverSettlementDTO> getDriverSettlementsByDriver(Long driverId) {
        if (driverId == null) {
            throw new InvalidDataException("El ID del chofer no puede ser nulo");
        }
        return driverSettlementRepository.findByDriverId(driverId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DriverSettlementDTO> getDriverSettlementsBySubmissionDate(LocalDate submissionDate) {
        if (submissionDate == null) {
            throw new InvalidDataException("La fecha de entrega no puede ser nula");
        }
        return driverSettlementRepository.findBySubmissionDate(submissionDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DriverSettlementDTO> getDriverSettlementsBySubmissionDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return driverSettlementRepository.findBySubmissionDateBetween(startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DriverSettlementDTO> getDriverSettlementsByDriverAndSubmissionDateRange(Long driverId, LocalDate startDate, LocalDate endDate) {
        if (driverId == null) {
            throw new InvalidDataException("El ID del chofer no puede ser nulo");
        }
        if (startDate == null || endDate == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return driverSettlementRepository.findByDriverIdAndSubmissionDateBetween(driverId, startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public DriverSettlementDTO updateDriverSettlement(DriverSettlementDTO settlement) {
        if (settlement.getId() == null) {
            throw new InvalidDataException("El ID no puede ser nulo para actualizar");
        }

        DriverSettlementEntity settlementEntity = driverSettlementRepository.findById(settlement.getId())
            .orElseThrow(() -> new ResourceNotFoundException(settlement.getId(), "Rendición de Chofer"));

        driverSettlementValidator.validateDriverSettlementSpecificFields(settlement);

        DriverEntity driver = driverService.getDriverEntityById(settlement.getDriverId());

        settlementEntity.setDriver(driver);
        settlementEntity.setTicketAmount(settlement.getTicketAmount());
        settlementEntity.setVoucherAmount(settlement.getVoucherAmount());
        settlementEntity.setVoucherDifference(settlement.getVoucherDifference());
        settlementEntity.setFinalBalance(settlement.getFinalBalance());
        settlementEntity.setSubmissionDate(settlement.getSubmissionDate());

        return convertToDTO(driverSettlementRepository.save(settlementEntity));
    }

    public void deleteDriverSettlement(Long id) {
        if (id == null) {
            throw new InvalidDataException("El ID no puede ser nulo");
        }

        DriverSettlementEntity settlement = driverSettlementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Rendición de Chofer"));

        driverSettlementRepository.delete(settlement);
    }

    /**
     * Calcula el total de tickets asociados a una rendición.
     * Suma todos los montos de los tickets de taxi que pertenecen a esta rendición.
     */
    public Double calculateTotalTickets(Long settlementId) {
        if (settlementId == null) {
            throw new InvalidDataException("El ID de la rendición no puede ser nulo");
        }

        // Verificar que la rendición existe
        driverSettlementRepository.findById(settlementId)
            .orElseThrow(() -> new ResourceNotFoundException(settlementId, "Rendición de Chofer"));

        // Sumar todos los montos de los tickets asociados a esta rendición usando el servicio
        return ticketTaxiService.getTicketTaxisBySettlement(settlementId).stream()
            .mapToDouble(ticket -> ticket.getAmount() != null ? ticket.getAmount() : 0.0)
            .sum();
    }

    /**
     * Calcula el saldo final de una rendición.
     * Fórmula: ticketAmount - voucherAmount + voucherDifference
     */
    public Double calculateFinalBalance(DriverSettlementDTO settlement) {
        if (settlement == null) {
            throw new InvalidDataException("La rendición no puede ser nula");
        }
        if (settlement.getTicketAmount() == null || settlement.getVoucherAmount() == null || settlement.getVoucherDifference() == null) {
            throw new InvalidDataException("Los montos de ticket, voucher y diferencia no pueden ser nulos");
        }

        return settlement.getTicketAmount() - settlement.getVoucherAmount() + settlement.getVoucherDifference();
    }

    private DriverSettlementEntity convertToEntity(DriverSettlementDTO settlement) {
        if (settlement == null) {
            return null;
        }

        DriverSettlementEntity entity = new DriverSettlementEntity();
        entity.setId(settlement.getId());
        entity.setTicketAmount(settlement.getTicketAmount());
        entity.setVoucherAmount(settlement.getVoucherAmount());
        entity.setVoucherDifference(settlement.getVoucherDifference());
        entity.setFinalBalance(settlement.getFinalBalance());
        entity.setSubmissionDate(settlement.getSubmissionDate());

        return entity;
    }

    private DriverSettlementEntity convertCreateDtoToEntity(DriverSettlementCreateDTO settlement) {
        if (settlement == null) {
            return null;
        }

        DriverSettlementEntity entity = new DriverSettlementEntity();
        entity.setTicketAmount(settlement.getTicketAmount());
        entity.setVoucherAmount(settlement.getVoucherAmount());
        entity.setVoucherDifference(settlement.getVoucherDifference());
        entity.setFinalBalance(settlement.getFinalBalance());
        entity.setSubmissionDate(settlement.getSubmissionDate());

        return entity;
    }

    private DriverSettlementDTO convertToDTO(DriverSettlementEntity settlement) {
        if (settlement == null) {
            return null;
        }

        return DriverSettlementDTO.builder()
            .id(settlement.getId())
            .driverId(settlement.getDriver().getId())
            .driver(driverService.getDriverById(settlement.getDriver().getId()))
            .ticketAmount(settlement.getTicketAmount())
            .voucherAmount(settlement.getVoucherAmount())
            .voucherDifference(settlement.getVoucherDifference())
            .finalBalance(settlement.getFinalBalance())
            .submissionDate(settlement.getSubmissionDate())
            .build();
    }

    /**
     * Obtiene la entidad DriverSettlementEntity por ID para uso interno de otros servicios.
     * Este método es package-private para mantener el encapsulamiento.
     */
    DriverSettlementEntity getDriverSettlementEntityById(Long id) {
        return driverSettlementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Rendición de Chofer"));
    }
}

