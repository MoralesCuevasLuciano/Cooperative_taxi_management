package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.PayrollSettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollSettlementRepository extends JpaRepository<PayrollSettlementEntity, Long> {
    Optional<PayrollSettlementEntity> findByMemberAccountIdAndYearMonth(Long memberAccountId, YearMonth yearMonth);
    boolean existsByMemberAccountIdAndYearMonth(Long memberAccountId, YearMonth yearMonth);
    List<PayrollSettlementEntity> findByMemberAccountId(Long memberAccountId);
    List<PayrollSettlementEntity> findByYearMonth(YearMonth yearMonth);
    List<PayrollSettlementEntity> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
}


