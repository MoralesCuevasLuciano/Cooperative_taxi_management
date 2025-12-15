package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.VehicleAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleAccountRepository extends JpaRepository<VehicleAccountEntity, Long> {

    Optional<VehicleAccountEntity> findByVehicleId(Long vehicleId);
    Optional<VehicleAccountEntity> findByVehicleIdAndActiveTrue(Long vehicleId);

    List<VehicleAccountEntity> findByActiveTrue();

    // Filtros por datos del vehículo
    Optional<VehicleAccountEntity> findByVehicleLicenseNumber(String licenseNumber);

    Optional<VehicleAccountEntity> findByVehicleLicensePlate(String licensePlate);

    List<VehicleAccountEntity> findByVehicleModelId(Long modelId);
}



