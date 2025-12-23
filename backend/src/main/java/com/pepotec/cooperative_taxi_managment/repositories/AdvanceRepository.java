package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.AdvanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdvanceRepository extends JpaRepository<AdvanceEntity, Long> {
    List<AdvanceEntity> findByMemberAccountId(Long memberAccountId);
    List<AdvanceEntity> findByMemberAccountIdAndActiveTrue(Long memberAccountId);
    List<AdvanceEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<AdvanceEntity> findByPayrollSettlementId(Long payrollSettlementId);
    List<AdvanceEntity> findByPayrollSettlementIsNull();
    void deleteByMovementId(Long movementId);
}


