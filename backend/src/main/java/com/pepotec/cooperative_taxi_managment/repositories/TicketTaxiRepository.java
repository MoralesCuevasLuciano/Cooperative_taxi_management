package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.TicketTaxiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketTaxiRepository extends JpaRepository<TicketTaxiEntity, Long> {
    List<TicketTaxiEntity> findByVehicleId(Long vehicleId);
    List<TicketTaxiEntity> findByRendicionId(Long rendicionId);
    List<TicketTaxiEntity> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<TicketTaxiEntity> findByCutDateBetween(LocalDate startDate, LocalDate endDate);
    Optional<TicketTaxiEntity> findByTicketNumber(String ticketNumber);
    List<TicketTaxiEntity> findByVehicleIdAndStartDateBetween(Long vehicleId, LocalDate startDate, LocalDate endDate);
    List<TicketTaxiEntity> findByVehicleIdAndCutDateBetween(Long vehicleId, LocalDate startDate, LocalDate endDate);
    List<TicketTaxiEntity> findByRendicionIdAndStartDateBetween(Long rendicionId, LocalDate startDate, LocalDate endDate);
    List<TicketTaxiEntity> findByRendicionIdAndCutDateBetween(Long rendicionId, LocalDate startDate, LocalDate endDate);
}

