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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_brand", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank(message = "The brand name cannot be empty")
    @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "Only letters, numbers and spaces are allowed")
    @Size(min = 2, max = 50, message = "The brand name must be between 2 and 50 characters")
    private String name;
}

