package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.DriverSettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DriverSettlementRepository extends JpaRepository<DriverSettlementEntity, Long> {
    List<DriverSettlementEntity> findByDriverId(Long driverId);
    
    List<DriverSettlementEntity> findBySubmissionDate(LocalDate submissionDate);
    
    List<DriverSettlementEntity> findBySubmissionDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<DriverSettlementEntity> findByDriverIdAndSubmissionDateBetween(Long driverId, LocalDate startDate, LocalDate endDate);
}

