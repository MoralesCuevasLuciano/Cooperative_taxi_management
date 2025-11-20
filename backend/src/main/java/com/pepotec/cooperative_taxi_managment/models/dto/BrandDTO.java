package com.pepotec.cooperative_taxi_managment.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandDTO {
    private Long id;

    @NotBlank(message = "El nombre de la marca no puede estar vacío")
    @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "Solo se permiten letras, números y espacios")
    @Size(min = 2, max = 50, message = "El nombre de la marca debe tener entre 2 y 50 caracteres")
    private String name;
}

