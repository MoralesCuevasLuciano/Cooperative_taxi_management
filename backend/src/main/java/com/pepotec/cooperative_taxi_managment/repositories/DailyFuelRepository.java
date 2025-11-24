package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.DailyFuelEntity;
import com.pepotec.cooperative_taxi_managment.models.enums.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyFuelRepository extends JpaRepository<DailyFuelEntity, Long> {
    List<DailyFuelEntity> findByVehicleId(Long vehicleId);
    List<DailyFuelEntity> findByDriverId(Long driverId);
    List<DailyFuelEntity> findByTicketIssueDateBetween(LocalDate startDate, LocalDate endDate);
    List<DailyFuelEntity> findBySubmissionDateBetween(LocalDate startDate, LocalDate endDate);
    List<DailyFuelEntity> findByFuelType(FuelType fuelType);
    List<DailyFuelEntity> findByVehicleIdAndTicketIssueDateBetween(Long vehicleId, LocalDate startDate, LocalDate endDate);
    List<DailyFuelEntity> findByDriverIdAndTicketIssueDateBetween(Long driverId, LocalDate startDate, LocalDate endDate);
    List<DailyFuelEntity> findByVehicleIdAndFuelType(Long vehicleId, FuelType fuelType);
    List<DailyFuelEntity> findByDriverIdAndFuelType(Long driverId, FuelType fuelType);
}

