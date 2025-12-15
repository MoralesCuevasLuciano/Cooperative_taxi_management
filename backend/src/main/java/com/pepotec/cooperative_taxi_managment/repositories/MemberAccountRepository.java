package com.pepotec.cooperative_taxi_managment.repositories;

import com.pepotec.cooperative_taxi_managment.models.entities.MemberAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberAccountRepository extends JpaRepository<MemberAccountEntity, Long> {

    Optional<MemberAccountEntity> findByMemberId(Long memberId);
    Optional<MemberAccountEntity> findByMemberIdAndActiveTrue(Long memberId);

    List<MemberAccountEntity> findByActiveTrue();

    // Filtros por datos de persona (Member hereda de Person)
    List<MemberAccountEntity> findByMemberFirstNameContainingIgnoreCase(String firstName);

    List<MemberAccountEntity> findByMemberFatherSurnameContainingIgnoreCase(String fatherSurname);

    Optional<MemberAccountEntity> findByMemberDni(String dni);
}



