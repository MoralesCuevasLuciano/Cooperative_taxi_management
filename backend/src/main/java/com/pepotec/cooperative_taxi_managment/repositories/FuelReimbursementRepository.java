package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.FuelReimbursementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuelReimbursementRepository extends JpaRepository<FuelReimbursementEntity, Long> {
    
    /**
     * Busca el FuelReimbursement para una cuenta de socio específica.
     * Con OneToOne, solo puede haber uno por cuenta.
     */
    Optional<FuelReimbursementEntity> findByMemberAccountId(Long memberAccountId);
}

