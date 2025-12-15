package com.pepotec.cooperative_taxi_managment.models.dto.model;

import com.pepotec.cooperative_taxi_managment.models.dto.brand.BrandDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModelDTO {
    private Long id;

    @NotBlank(message = "El nombre del modelo no puede estar vacío")
    @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "Solo se permiten letras, números y espacios")
    @Size(min = 2, max = 100, message = "El nombre del modelo debe tener entre 2 y 100 caracteres")
    private String name;

    @NotNull(message = "El año no puede ser nulo")
    @Min(value = 1900, message = "El año debe ser mayor o igual a 1900")
    @Max(value = 2100, message = "El año debe ser menor o igual a 2100")
    // Note: Validation for "year cannot be future" will be implemented in ModelValidator
    private Integer year;

    @Valid
    @NotNull(message = "La marca no puede ser nula")
    private BrandDTO brand;
}


