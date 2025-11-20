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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Entity
@Table(name = "models")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_model", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "The model name cannot be empty")
    @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "Only letters, numbers and spaces are allowed")
    @Size(min = 2, max = 100, message = "The model name must be between 2 and 100 characters")
    private String name;

    @Column(name = "year", nullable = false)
    @NotNull(message = "The year cannot be null")
    @Min(value = 1900, message = "The year must be greater than or equal to 1900")
    @Max(value = 2100, message = "The year must be less than or equal to 2100")
    // Note: Validation for "year cannot be future" will be implemented in ModelValidator
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "id_brand", nullable = false)
    @NotNull(message = "The brand cannot be null")
    private BrandEntity brand;
}

