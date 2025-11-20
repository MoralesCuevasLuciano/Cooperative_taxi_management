package com.pepotec.cooperative_taxi_managment.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.pepotec.cooperative_taxi_managment.services.BrandService;
import com.pepotec.cooperative_taxi_managment.models.dto.BrandDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@ApiResponses(
    value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    }
)
@Tag(
    name = "Brands",
    description = "API para la gestión de marcas de vehículos"
)
@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Operation(
        summary = "Crear una nueva marca",
        description = "Crea una nueva marca con la información proporcionada.",
        tags = {"Brands"},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Marca creada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BrandDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe una marca con el mismo nombre")
        }
    )
    @PostMapping("/create")
    public ResponseEntity<BrandDTO> createBrand(@Valid @RequestBody BrandDTO brand) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(brandService.createBrand(brand));
    }

    @Operation(
        summary = "Actualizar una marca existente",
        description = "Actualiza los datos de una marca existente por su ID.",
        tags = {"Brands"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Marca actualizada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BrandDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe una marca con el mismo nombre")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<BrandDTO> updateBrand(
        @PathVariable Long id,
        @Valid @RequestBody BrandDTO brand
    ) {
        brand.setId(id);
        return ResponseEntity.ok(brandService.updateBrand(brand));
    }

    @Operation(
        summary = "Eliminar una marca",
        description = "Elimina una marca por su ID.",
        tags = {"Brands"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Marca eliminada exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener una marca por ID",
        description = "Busca y retorna una marca por su ID.",
        tags = {"Brands"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Marca encontrada",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BrandDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
        }
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    @Operation(
        summary = "Obtener todas las marcas",
        description = "Retorna una lista de todas las marcas.",
        tags = {"Brands"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de marcas retornada exitosamente",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BrandDTO.class)
                )
            )
        }
    )
    @GetMapping("/list")
    public ResponseEntity<List<BrandDTO>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }
}

