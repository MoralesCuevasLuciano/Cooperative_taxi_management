package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.SubscriberAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriberAccountRepository extends JpaRepository<SubscriberAccountEntity, Long> {

    Optional<SubscriberAccountEntity> findBySubscriberId(Long subscriberId);
    Optional<SubscriberAccountEntity> findBySubscriberIdAndActiveTrue(Long subscriberId);

    List<SubscriberAccountEntity> findByActiveTrue();

    // Filtros por datos de persona (Subscriber hereda de Person)
    List<SubscriberAccountEntity> findBySubscriberFirstNameContainingIgnoreCase(String firstName);

    List<SubscriberAccountEntity> findBySubscriberFatherSurnameContainingIgnoreCase(String fatherSurname);

    Optional<SubscriberAccountEntity> findBySubscriberDni(String dni);
}



