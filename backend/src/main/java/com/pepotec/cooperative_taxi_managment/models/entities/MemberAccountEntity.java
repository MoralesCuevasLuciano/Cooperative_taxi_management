package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Cuenta asociada a un socio de la cooperativa.
 */
@Entity
@Table(name = "member_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MemberAccountEntity extends AbstractAccountEntity {

    @OneToOne
    @JoinColumn(name = "id_member", nullable = false, unique = true)
    @NotNull(message = "The member cannot be null")
    private MemberEntity member;
}


