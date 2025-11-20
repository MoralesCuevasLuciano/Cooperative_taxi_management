package com.pepotec.cooperative_taxi_managment.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicle", unique = true, nullable = false)
    private Long id;

    @Column(name = "license_plate", nullable = false, unique = true)
    @NotBlank(message = "The license plate cannot be empty")
    @Pattern(regexp = "^[A-Z]{3}[0-9]{3}$|^[A-Z]{2}[0-9]{3}[A-Z]{2}$", 
             message = "The license plate must have format AAA123 or AB123CD")
    @Size(min = 6, max = 7, message = "The license plate must have 6 or 7 characters")
    private String licensePlate;

    @Column(name = "license_number", nullable = false, unique = true)
    @NotBlank(message = "The license number cannot be empty")
    @Pattern(regexp = "^[0-9]+$", message = "The license number must contain only digits")
    @Size(min = 1, max = 20, message = "The license number must have between 1 and 20 digits")
    private String licenseNumber;

    @Column(name = "engine_number", nullable = false, unique = true)
    @NotBlank(message = "The engine number cannot be empty")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "The engine number must contain only uppercase letters and numbers")
    @Size(min = 5, max = 30, message = "The engine number must have between 5 and 30 characters")
    private String engineNumber;

    @Column(name = "chassis_number", nullable = false, unique = true)
    @NotBlank(message = "The chassis number cannot be empty")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "The chassis number must contain only uppercase letters and numbers")
    @Size(min = 10, max = 30, message = "The chassis number must have between 10 and 30 characters")
    private String chassisNumber;

    @Column(name = "vtv_expiration_date", nullable = false)
    @NotNull(message = "The VTV expiration date cannot be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate vtvExpirationDate;

    @Column(name = "active", nullable = false)
    @NotNull(message = "The active status cannot be null")
    @Builder.Default
    private Boolean active = true;

    @Column(name = "leave_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Builder.Default
    private LocalDate leaveDate = null;

    @ManyToOne
    @JoinColumn(name = "id_model", nullable = false)
    @NotNull(message = "The model cannot be null")
    private ModelEntity model;
}

