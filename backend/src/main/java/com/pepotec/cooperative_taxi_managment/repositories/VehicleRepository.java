package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
    Optional<VehicleEntity> findByLicensePlate(String licensePlate);
    Optional<VehicleEntity> findByLicenseNumber(String licenseNumber);
    Optional<VehicleEntity> findByEngineNumber(String engineNumber);
    Optional<VehicleEntity> findByChassisNumber(String chassisNumber);
    List<VehicleEntity> findByModelId(Long modelId);
    List<VehicleEntity> findByActiveTrue();
    List<VehicleEntity> findByActiveTrueAndLeaveDateIsNull();
}

