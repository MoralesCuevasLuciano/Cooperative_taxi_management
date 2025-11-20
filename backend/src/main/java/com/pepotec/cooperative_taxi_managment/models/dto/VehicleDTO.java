package com.pepotec.cooperative_taxi_managment.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDTO {
    private Long id;

    @NotBlank(message = "La patente no puede estar vacía")
    @Pattern(regexp = "^[A-Z]{3}[0-9]{3}$|^[A-Z]{2}[0-9]{3}[A-Z]{2}$", 
             message = "La patente debe tener formato AAA123 o AB123CD")
    @Size(min = 6, max = 7, message = "La patente debe tener 6 o 7 caracteres")
    private String licensePlate;

    @NotBlank(message = "El número de licencia no puede estar vacío")
    @Pattern(regexp = "^[0-9]+$", message = "El número de licencia debe contener solo dígitos")
    @Size(min = 1, max = 20, message = "El número de licencia debe tener entre 1 y 20 dígitos")
    private String licenseNumber;

    @NotBlank(message = "El número de motor no puede estar vacío")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "El número de motor debe contener solo letras mayúsculas y números")
    @Size(min = 5, max = 30, message = "El número de motor debe tener entre 5 y 30 caracteres")
    private String engineNumber;

    @NotBlank(message = "El número de chasis no puede estar vacío")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "El número de chasis debe contener solo letras mayúsculas y números")
    @Size(min = 10, max = 30, message = "El número de chasis debe tener entre 10 y 30 caracteres")
    private String chassisNumber;

    @NotNull(message = "La fecha de vencimiento de VTV no puede ser nula")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate vtvExpirationDate;

    @NotNull(message = "El estado activo no puede ser nulo")
    private Boolean active;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate leaveDate;

    @Valid
    @NotNull(message = "El modelo no puede ser nulo")
    private ModelDTO model;
}

