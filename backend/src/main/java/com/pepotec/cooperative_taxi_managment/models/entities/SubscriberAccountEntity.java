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
 * Cuenta asociada a un abonado.
 */
@Entity
@Table(name = "subscriber_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SubscriberAccountEntity extends AbstractAccountEntity {

    @OneToOne
    @JoinColumn(name = "id_subscriber", nullable = false, unique = true)
    @NotNull(message = "The subscriber cannot be null")
    private SubscriberEntity subscriber;
}


