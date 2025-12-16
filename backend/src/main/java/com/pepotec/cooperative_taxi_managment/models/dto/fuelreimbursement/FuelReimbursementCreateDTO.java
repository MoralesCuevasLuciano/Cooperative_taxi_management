package com.pepotec.cooperative_taxi_managment.models.dto.fuelreimbursement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para crear registros de reintegro de combustible.
 * El ID de memberAccount se recibe por path.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FuelReimbursementCreateDTO {

    private Double accumulatedAmount;
}

